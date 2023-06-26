package com.aptech.proj4.service;

import java.util.List;

import org.springframework.core.io.Resource;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.dto.ProjectDto;
import com.aptech.proj4.model.Document;

public interface DocumentService {
    DocumentDto createDocument(DocumentDto document, ProjectDto projectDto);

    boolean deleteDocument(String id);

    List<Document> findFilesByName(String file); // use string `files`

    List<Document> getAllDocuments();

    Resource loadDocumentFile(String fileId); // get Document from resource

    String getDocumentFileUrl(String fileId); // create url for dowload
}
