package com.aptech.proj4.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aptech.proj4.model.Document;

public interface DocumentRepository extends CrudRepository<Document, String> {
    Optional<Document> findByFilesContaining(String files);
}