package com.aptech.proj4.service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.model.Document;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.repository.DocumentRepository;
import com.aptech.proj4.repository.ProjectRepository;
import com.aptech.proj4.utils.FileUploadUtil;

@Service
public class DocumentServiceImpl implements DocumentService {
  @Autowired
  DocumentRepository documentRepository;
  @Autowired
  ProjectRepository projectRepository;

  @Override
  public DocumentDto createDocument(DocumentDto documentDto, MultipartFile file) {
    if (documentDto == null || documentDto.getDescription() == null ||
        documentDto.getCreateAt() == null || documentDto.getProject_id() == null) {
      throw new IllegalArgumentException("Invalid documentDto");
    }
    // Random ID
    String documentId = UUID.randomUUID().toString();
    // Find Project ID
    Optional<Project> projectOptional = projectRepository.findById(documentDto.getProject_id());
    Project project = projectOptional.orElseThrow(() -> new RuntimeException("Project not found"));
    // Create and save Document
    Document document = new Document()
        .setId(documentId)
        .setDescription(documentDto.getDescription())
        .setFiles(file.getOriginalFilename())
        .setCreatedAt(documentDto.getCreateAt())
        .setProject(project);
    Document savedDocument = documentRepository.save(document);
    // Check return value
    if (savedDocument == null) {
      throw new RuntimeException("Failed to save document");
    }
    // Save the file
    try {
      String uploadDir = "upload directory"; // TODO: add Upload Directory later
      String fileName = savedDocument.getId() + "-" + file.getOriginalFilename();
      FileUploadUtil.saveFile(uploadDir, fileName, file);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save file", e);
    }
    // Create documentDto
    DocumentDto savedDocumentDto = new DocumentDto();
    savedDocumentDto.setId(savedDocument.getId());
    savedDocumentDto.setDescription(savedDocument.getDescription());
    savedDocumentDto.setFiles(savedDocument.getFiles());
    savedDocumentDto.setProject_id(savedDocument.getProject().getId());
    savedDocumentDto.setCreateAt(savedDocument.getCreatedAt());

    return savedDocumentDto;
  }

  @Override
  public boolean deleteDocument(String id) {
    Document document = documentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Document ID not found"));
    documentRepository.delete(document);
    return true;
  }

  @Override
  public DocumentDto findFileByName(String file) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findFileByName'");
  }

  @Override
  public String getCreatedAt(Document document) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCreatedAt'");
  }

  @Override
  public List<Document> getAllDocuments() {
    List<Document> documents = (List<Document>) documentRepository.findAll();
    return documents;
  }

}
