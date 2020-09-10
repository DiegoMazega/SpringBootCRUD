package com.diegomazega.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	public URI uploadFile(MultipartFile multiPartFile) {
		try {
			String fileName = multiPartFile.getOriginalFilename();
			InputStream inputStream;
			inputStream = multiPartFile.getInputStream();
			String contentType = multiPartFile.getContentType();
			return uploadFile(inputStream, fileName, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: " + e.getMessage());
		}
	}

	public URI uploadFile(InputStream inputStream, String fileName, String contentType) {
		try {
			ObjectMetadata omd = new ObjectMetadata();
			omd.setContentType(contentType);
			LOG.info("Iniciando o Upload");
			s3client.putObject(bucketName, fileName, inputStream, omd);
			LOG.info("Fim do Upload");
			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI");
		}
	}
}
