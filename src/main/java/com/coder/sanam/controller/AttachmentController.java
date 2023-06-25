package com.coder.sanam.controller;

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

@RestController
public class AttachmentController {

	private AttachmentService attachmentService;

	public AttachmentController(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@PostMapping("/uploadFile")
	public ResponseData uploadFile(@RequestParam("file") MultipartFile file) {
		Attachment attachment = null;
		String downloadURL = "";
		System.out.println(file.getOriginalFilename());
		attachment = attachmentService.saveAttachment(file);
		downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(attachment.getId())
				.toUriString();

		return new ResponseData(file.getSize(), downloadURL, attachment.getFileName(), file.getContentType());

	}

	@GetMapping("/download/{fileId}")
	public ResponseEntity<Resource> downloadAttachment(@PathVariable String fileId) {
		Attachment attachment = null;
		attachment = attachmentService.getAttachmentById(fileId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
				.body(new ByteArrayResource(attachment.getData()));
	}

}
