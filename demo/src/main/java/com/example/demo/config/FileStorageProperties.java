package com.example.demo.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "file", ignoreUnknownFields = true, ignoreInvalidFields = true)
@Configuration
public class FileStorageProperties{
    private String uploadDir;
    private String tempExportExcel;
    private String libreOfficePath;
    public String getUploadDir() {
        return this.uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    public String getTempExportExcel() {
        return this.tempExportExcel;
    }
    public void setTempExportExcel(String tempExportExcel) {
        this.tempExportExcel = tempExportExcel;
    }
    public String getLibreOfficePath() {
        return this.libreOfficePath;
    }
    public void setLibreOfficePath(String libreOfficePath) {
        this.libreOfficePath = libreOfficePath;
    }
}
