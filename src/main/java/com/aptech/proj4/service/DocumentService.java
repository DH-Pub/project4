package com.aptech.proj4.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.model.Document;

public interface DocumentService {
    DocumentDto createDocument(DocumentDto document, MultipartFile file);

    boolean deleteDocument(String id);

    DocumentDto findFileByName(String file); // use string `files`

    String getCreatedAt(Document document); // get created time

    List<Document> getAllDocuments();

    /*
     * TODO: add implement this
     */
}
