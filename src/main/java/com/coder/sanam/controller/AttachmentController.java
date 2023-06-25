package com.coder.sanam.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coder.sanam.ResponseData;
import com.coder.sanam.entity.Attachment;
import com.coder.sanam.service.AttachmentService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AttachmentController {

	private AttachmentService attachmentService;

	@Value("${fileStorage.enabled}")
	private boolean fileStorageEnabled;

	public AttachmentController(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@PostMapping("/uploadFile")
	public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		String downloadURL = "";
		long fileSize = 0;
		String fileType = "";
		String fileName = "";
		if (fileStorageEnabled) {
			try {
				String dest = "D:\\Shubham\\data\\";
				String destFilePath = dest + file.getOriginalFilename();

				file.transferTo(new File(destFilePath));
				System.out.println("File Uploaded successfully!!!");
				downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
						.path(file.getOriginalFilename()).toUriString();
				fileSize = file.getSize();
				fileType = file.getContentType();
				fileName = file.getOriginalFilename();
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {

			Attachment attachment = null;
			System.out.println(file.getOriginalFilename());
			attachment = attachmentService.saveAttachment(file);
			downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
					.path(attachment.getId()).toUriString();
			fileSize = file.getSize();
			fileType = file.getContentType();
			fileName = attachment.getFileName();

		}
		return new ResponseData(fileSize, downloadURL, fileName, fileType);

	}

	@GetMapping("/download/{fileId}")
	public void downloadAttachment(@PathVariable String fileId, HttpServletResponse response) throws IOException {
		if(fileStorageEnabled) {
			String filepath = "D:\\Shubham\\data\\" + fileId;
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(new File(filepath));
			response.setContentType("application/octet-stream");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment ; filename = \"" + file.getName() + "\"");
			OutputStream outputStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			fileInputStream.close();
			outputStream.close();
		}else {
			Attachment attachment = null;
			attachment = attachmentService.getAttachmentById(fileId);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment ; filename = \"" + attachment.getFileName()  + "\"");
			ByteArrayResource byteArrayResource = new ByteArrayResource(attachment.getData());
			response.setContentType("application/octet-stream");

	        // Set the content length
	        response.setContentLength((int) byteArrayResource.contentLength());

	        // Set the body content
	        InputStream inputStream = byteArrayResource.getInputStream();
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            response.getOutputStream().write(buffer, 0, bytesRead);
	        }
	        inputStream.close();
			
		}
		



	}

}
