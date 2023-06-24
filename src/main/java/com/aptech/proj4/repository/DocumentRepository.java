package com.aptech.proj4.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Document;

public interface DocumentRepository extends CrudRepository<Document, String> {
    List<Document> findByFilesContaining(String files);
}