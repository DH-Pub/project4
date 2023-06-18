package com.aptech.proj4.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DocumentDto createDocument(DocumentDto documentDto, MultipartFile file) {
    if (documentDto == null || documentDto.getDescription() == null || documentDto.getProject_id() == null) {
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
    Optional<Document> document = Optional.ofNullable(documentRepository.findByName(file).get());
    if (document.isPresent()) {
      return modelMapper.map(document.get(), DocumentDto.class);
    }
    throw new RuntimeException("Document name not found");
  }

  @Override
  public List<Document> getAllDocuments() {
    List<Document> documents = (List<Document>) documentRepository.findAll();
    return documents;
  }

  @Override
  public Resource loadDocumentFile(String fileId) {
    // Load the document from the database
    Document document = documentRepository.findById(fileId)
        .orElseThrow(() -> new NoSuchElementException("Document ID not found"));

    // Get the file name
    String fileName = document.getFiles();

    try {
      // Build the file path
      String uploadDir = "upload directory"; // TODO: add Upload Directory later
      String filePath = uploadDir + "/" + document.getId() + "-" + fileName;

      // Load the file as a resource
      Resource resource = new UrlResource(filePath);

      // Check if the file exists and is readable
      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Failed to load document file: " + fileName);
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Failed to load document file: " + fileName, e);
    }
  }

  @Override
  public String getDocumentFileUrl(String fileId) {
    // Generate the download URL for the document file
    String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/documents/download/")
        .path(fileId)
        .toUriString();

    return downloadUrl;
  }

}
