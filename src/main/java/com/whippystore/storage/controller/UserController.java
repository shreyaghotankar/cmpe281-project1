package com.whippystore.storage.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.whippystore.storage.dao.UserUploadFileRecord;
import com.whippystore.storage.dao.UserUploadFileRecordDaoImpl;

@Controller
public class UserController {

	@Autowired
	private AmazonClient amazonClient;

	@Autowired
	private UserUploadFileRecordDaoImpl dao;

	@Value("${cloudfront.uri}")
	private String cloudfrontUri;

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);


	/*
	 * @Autowired private UserUploadFileRecordDaoImpl dao;
	 */
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("description") String description,
			RedirectAttributes attributes, Authentication authentication) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// save the file in S3
		try {

			String fileUrl = amazonClient.uploadFile(file);

			createOrUpdateCustomerFiles(fileName, description, fileUrl,
					((DefaultOidcUser) authentication.getPrincipal()).getEmail(), file.getSize(),
					((DefaultOidcUser) authentication.getPrincipal()).getGivenName(),
					((DefaultOidcUser) authentication.getPrincipal()).getFamilyName());

			// return success response
			attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		} catch (MaxUploadSizeExceededException e) {
			
			attributes.addFlashAttribute("errorMessage",
					"File  upload failed " + fileName + "!." + ".Reason=" + e.getMessage());
			LOG.error("Error Message: " + e.getMessage());
		}

		return "redirect:/";
	}

	private void createOrUpdateCustomerFiles(String filename, String description, String path, String userName,
			Long fileSize, String firstname, String lastname) {
		try {
			UserUploadFileRecord userUploadFileRecord = new UserUploadFileRecord();

			userUploadFileRecord.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			userUploadFileRecord.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

			userUploadFileRecord.setDescription(description);
			userUploadFileRecord.setFileSize(fileSize.toString());
			userUploadFileRecord.setFilename(filename);
			userUploadFileRecord.setUserName(userName);
			userUploadFileRecord.setPath(path);
			userUploadFileRecord.setFirstname(firstname);
			userUploadFileRecord.setLastname(lastname);
			dao.createOrUpdate(userUploadFileRecord);
		} catch (Exception e) {
		
			LOG.error("Error Message: " + e.getMessage());

		}
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("id") String id, RedirectAttributes attributes, Authentication authentication) {
		try {
			UserUploadFileRecord record = dao.findById(Integer.parseInt(id));
			amazonClient.deleteFile(record.getPath());
			dao.deleteRecord(Integer.parseInt(id));
			attributes.addFlashAttribute("message", "Delete Successful");

		} catch (Exception e) {
			attributes.addFlashAttribute("errorMessage", "Delete Failed" + "Error: " + e.getMessage());
			LOG.error("Error Message: " + e.getMessage());
		
		}

		return "redirect:/";
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam("name") String name) throws IOException {

		URL url = new URL(cloudfrontUri + name);
		byte[] bytes = IOUtils.toByteArray(url);

		String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);
       
			
		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/edit")
	public String editFile(@RequestParam("id") String id, @RequestParam("file") MultipartFile file,
			RedirectAttributes attributes, Authentication authentication) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file.");
			return "redirect:/";
		}
		UserUploadFileRecord record = dao.findById(Integer.parseInt(id));
		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		if (!(record.getFilename().equalsIgnoreCase(fileName))) {
			attributes.addFlashAttribute("errorMessage",
					"File  Edit failed. " + " Reason=" + "Filename does not match to " + record.getFilename());

		} else {
			try {

				String fileUrl = amazonClient.uploadFile(file);
				Long fileSize = new Long(file.getSize());
				record.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
				record.setFileSize(fileSize.toString());

				editCustomerFiles(record);

				// return success response
				attributes.addFlashAttribute("message", "You successfully edited " + fileName + '!');

			} catch (MaxUploadSizeExceededException e) {
				
				attributes.addFlashAttribute("message",
						"File  Edit failed " + fileName + "!." + ".Reason=" + e.getMessage());
				LOG.error("Error Message: " + e);
			}

		}

		return "redirect:/";
	}

	private void editCustomerFiles(UserUploadFileRecord record) {
		try {

			dao.createOrUpdate(record);

		} catch (Exception e) {
			LOG.error("Error Message: " + e.getMessage());

		}

	}
}
