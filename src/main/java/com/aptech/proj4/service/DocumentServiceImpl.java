package com.aptech.proj4.service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Document;
import com.aptech.proj4.model.Project;
import com.aptech.proj4.repository.DocumentRepository;
import com.aptech.proj4.repository.ProjectRepository;

@Service
public class DocumentServiceImpl implements DocumentService {
  @Autowired
  DocumentRepository documentRepository;
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DocumentDto createDocument(DocumentDto documentDto, ProjectDto projectDto) {
    Project project = projectRepository.findById(projectDto.getId().toString())
        .orElseThrow(() -> new RuntimeException("Project ID not found"));

    Document newDocument = new Document()
        .setId(Long.toString(System.currentTimeMillis()))
        .setDescription(documentDto.getDescription());
    String file = documentDto.getFiles() == null ? null : documentDto.getFiles();
    newDocument.setFiles(file);
    newDocument.setProject(project);
    documentRepository.save(newDocument);

    documentDto.setFiles(file);
    return documentDto;
  }

  @Override
  public boolean deleteDocument(String id) {
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
      String uploadDir = "documents/upload/";
      String filePath = uploadDir + fileName;
      Resource resource = new FileSystemResource(filePath);
      if (resource.exists()) {
        return resource;
      } else {
        throw new RuntimeException("Failed to load document file: " + fileName);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load document file: " + fileName, e);
    }
  }

  @Override
  public String getDocumentFileUrl(String fileId) {
    String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/documents/download/")
        .path(fileId)
        .toUriString();
    return downloadUrl;
  }

}
