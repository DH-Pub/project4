package com.aptech.proj4.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Document;

public interface DocumentService {
    DocumentDto createDocument(DocumentDto document, ProjectDto projectDto);

    boolean deleteDocument(String id, String authentication);

    // DocumentDto findFileByName(String file); // use string `files`

    List<Document> getAllDocuments(String authentication);

    Resource loadDocumentFile(String fileId, String authentication); // get Document from resource

    String getDocumentFileUrl(String fileId, String authentication); // create url for dowload
}
