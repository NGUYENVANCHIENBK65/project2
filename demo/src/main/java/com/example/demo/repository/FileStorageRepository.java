package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Image;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<Image, Integer>{
    
}
