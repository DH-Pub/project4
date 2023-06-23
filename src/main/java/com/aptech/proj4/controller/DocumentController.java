package com.aptech.proj4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Document;
import com.aptech.proj4.service.DocumentService;
import com.aptech.proj4.utils.FileUploadUtil;

@RestController
@RequestMapping("/document")
public class DocumentController {
  @Autowired
  private DocumentService documentService;

  @PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> createDocument(
      @RequestPart(value = "file", required = true) MultipartFile file,
      @RequestPart(value = "document") DocumentDto documentDto,
      @RequestPart(value = "project") ProjectDto projectDto) {
    try {
      DocumentDto document = new DocumentDto();
      if (file != null) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        documentDto.setFiles(fileName);
        document = documentService.createDocument(documentDto, projectDto);

        String uploadDir = "documents/upload";
        FileUploadUtil.saveFile(uploadDir, document.getFiles(), file);
      } else {
        documentDto.setFiles(null);
        document = documentService.createDocument(documentDto, projectDto);
      }
      return ResponseEntity.ok(document);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project ID not found.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not upload file.");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDocument(@PathVariable("id") String id) {
    boolean deleted = documentService.deleteDocument(id);
    if (deleted) {
      return ResponseEntity.status(HttpStatus.OK).body("Document deleted successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // @GetMapping("/{file}")
  // public ResponseEntity<DocumentDto> findFileByName(@PathVariable("file")
  // String file) {
  // DocumentDto documentDto = documentService.findFileByName(file);
  // return ResponseEntity.ok(documentDto);
  // }

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
