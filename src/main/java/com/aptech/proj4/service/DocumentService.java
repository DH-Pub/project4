package com.aptech.proj4.service;

import java.util.List;

import com.aptech.proj4.dto.DocumentDto;
import com.aptech.proj4.model.Document;

public interface DocumentService {
    DocumentDto createDocument(DocumentDto document);

    boolean deleteDocument(String id);

    DocumentDto findFileByName(String file); // use string `files`

    String getCreatedAt(Document document); // get created time

    List<Document> getAllDocuments();

    /*
     * TODO: add implement this
     */
}
