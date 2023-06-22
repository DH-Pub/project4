package com.aptech.proj4.service;

import java.io.IOException;
import java.net.MalformedURLException;
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
import com.aptech.proj4.dto.ProjectDto;
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
  public DocumentDto createDocument(DocumentDto documentDto, ProjectDto projectDto, String authentication) {
    Optional<Project> project = projectRepository.findById(projectDto.getId());
    if (project.isPresent()) {
      Document newDocument = new Document()
          .setId(Long.toString(System.currentTimeMillis()))
          .setDescription(documentDto.getDescription());
      String file = documentDto.getFiles() == null ? null : documentDto.getFiles();
      newDocument.setFiles(file);
      documentRepository.save(newDocument);

      documentDto.setFiles(file);
      return documentDto;
    } else {
      throw new RuntimeException("Project ID not found");
    }
  }

  @Override
  public boolean deleteDocument(String id, String authentication) {
    Document document = documentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Document ID not found"));
    documentRepository.delete(document);
    return true;
  }

  // @Override
  // public DocumentDto findFileByName(String file) {
  // Optional<Document> document =
  // Optional.ofNullable(documentRepository.findByName(file).get());
  // if (document.isPresent()) {
  // return modelMapper.map(document.get(), DocumentDto.class);
  // }
  // throw new RuntimeException("Document name not found");
  // }

  @Override
  public List<Document> getAllDocuments(String authentication) {
    List<Document> documents = (List<Document>) documentRepository.findAll();
    return documents;
  }

  @Override
  public Resource loadDocumentFile(String fileId, String authentication) {
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
  public String getDocumentFileUrl(String fileId, String authentication) {
    // Generate the download URL for the document file
    String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/documents/download/")
        .path(fileId)
        .toUriString();

    return downloadUrl;
  }

}
