package com.coder.sanam.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.coder.sanam.entity.Attachment;
import com.coder.sanam.repo.AttachmentRepository;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	private AttachmentRepository attachmentRepository;

	public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@Override
	public Attachment saveAttachment(MultipartFile file) {
		Attachment attachment = null;
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if(fileName.contains("..")) {
				throw new Exception("FileName cannot have path sequences..");
			}
			attachment = new Attachment(fileName,file.getContentType(),file.getBytes());
			attachmentRepository.save(attachment);
			
		}catch(Exception e) {
			System.err.println("Exception while saving the attachment..");
		}
		return attachment;
	}

	@Override
	public Attachment getAttachmentById(String fileId) {
		Attachment attachment = null;
		try {
			attachment = attachmentRepository.findById(fileId).orElseThrow(()->  new Exception("file Not found with id:"+fileId));
			
		}catch(Exception e) {
			System.err.println(e);
		}
		return attachment;
	}
	
	
	
	
}
