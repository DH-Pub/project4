package com.aptech.proj4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.model.Document;
import com.aptech.proj4.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {
  @Autowired
  private DocumentService documentService;

  @PostMapping
  public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto,
      @RequestParam("file") MultipartFile file) {
    DocumentDto savedDocument = documentService.createDocument(documentDto, file);
    return ResponseEntity.ok(savedDocument);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDocument(@PathVariable("id") String id) {
    boolean deleted = documentService.deleteDocument(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{file}")
  public ResponseEntity<DocumentDto> findFileByName(@PathVariable("file") String file) {
    DocumentDto documentDto = documentService.findFileByName(file);
    return ResponseEntity.ok(documentDto);
  }

  @GetMapping
  public ResponseEntity<List<Document>> getAllDocuments() {
    List<Document> documents = documentService.getAllDocuments();
    return ResponseEntity.ok(documents);
  }

  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadDocument(@PathVariable("fileId") String fileId) {
    Resource resource = documentService.loadDocumentFile(fileId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
