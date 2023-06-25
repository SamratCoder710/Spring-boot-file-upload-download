package com.coder.sanam.service;

import org.springframework.web.multipart.MultipartFile;

import com.coder.sanam.entity.Attachment;

public interface AttachmentService {

	Attachment saveAttachment(MultipartFile file);

	Attachment getAttachmentById(String fileId);

}
