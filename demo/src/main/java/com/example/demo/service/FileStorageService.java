package com.example.demo.service;


import com.example.demo.config.FileStorageProperties;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.activation.MimetypesFileTypeMap;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final String tempExportExcel;
	private final String libreOfficePath;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws Exception {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		this.tempExportExcel = fileStorageProperties.getTempExportExcel();
		this.libreOfficePath = fileStorageProperties.getLibreOfficePath();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new Exception(
					"Could not create the directory where the uploaded files will be stored.\n" + ex.getMessage());
		}
	}
    public String getTempExportExcel() {
		return this.tempExportExcel;
	}

	public String getLibreOfficePath() {
		return this.libreOfficePath;
	}
    public Resource loadFileAsResource(String fileName) throws Exception {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new Exception("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new Exception("File not found " + fileName + "\n" + ex.getMessage());
		}
	}

	public HttpHeaders loadHttpHeaders(Resource resource) throws IOException {
		HttpHeaders headers = new HttpHeaders(null);
		headers.add(HttpHeaders.CONTENT_TYPE, new MimetypesFileTypeMap().getContentType(resource.getFile()));
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		return headers;
	}
    public String saveFile(String relativePath, MultipartFile multipartFile) throws Exception {
		Path folderPath = Paths.get(this.fileStorageLocation.normalize().toString(), relativePath).normalize();
		String fileName = multipartFile.getOriginalFilename();
		Path filePath = folderPath.resolve(fileName);
		if (!folderPath.toFile().exists()) {
			try {
				FileUtils.forceMkdirParent(filePath.toFile());
			} catch (IOException e) {
				throw new Exception("folder.can.not.create");
			}
		}
		while (filePath.toFile().exists()) {
			String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
			filePath = folderPath.resolve(prefix + "_" + fileName);
		}
		try {
			multipartFile.transferTo(filePath);
		} catch (IllegalStateException | IOException e) {
			throw new Exception("file.can.not.create");
		}
		return this.fileStorageLocation.normalize().relativize(filePath).normalize().toString().replace("\\", "/");
	}
    public Boolean deleteFile(List<String> relativeFilePaths) {
		List<File> files = new ArrayList<>();
		for (String relativeFilePath : relativeFilePaths) {
			Path filePath = Paths.get(this.fileStorageLocation.normalize().toString(), relativeFilePath).normalize();
			if (!filePath.toFile().exists()) {
				return false;
			}
			files.add(filePath.toFile());
		}
		for (File file : files) {
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public String getAbsolutePath(String relativePath) {
		return Paths.get(this.fileStorageLocation.normalize().toString(), relativePath).normalize().toFile()
				.getAbsolutePath();
	}
}
