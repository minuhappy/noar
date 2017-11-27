package com.minu.core.system.handler;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.minu.core.exception.ServerException;
import com.minu.core.exception.ServiceException;
import com.minu.core.system.ConfigConstants;
import com.minu.core.system.Constants;
import com.minu.core.util.PropertyUtil;
import com.minu.core.util.ValueUtil;

public class FileServiceHandler {
	public String fileUpload(String id, String path, MultipartFile... files) {
		
		StringBuilder savePath = new StringBuilder();
		savePath.append(PropertyUtil.getProperty(ConfigConstants.FILE_BASE_PATH, "C:\\"));
		savePath.append(path);

		validationCheck(files);

		uploadFile(id, path, files);

		return id;
	}

	private void validationCheck(MultipartFile... files) {
		if (ValueUtil.isEmpty(files)) {
			throw new ServiceException("Failed to upload. File is empty.");
		}

		// Limit File Size
		Long limitSize = ValueUtil.toLong(PropertyUtil.getProperty(ConfigConstants.FILE_LIMIT_UPLOAD_SIZE, "10"));
		limitSize *= Constants.MB;
		Long currentSize = 0L;

		for (MultipartFile file : files) {
			// File Size Check : Container에서 지정한 범위 안에 있을 경우만 해당.(Container 설정 :
			// application.properties)
			currentSize += ValueUtil.toLong(file.getSize());
			if (currentSize > limitSize) {
				throw new ServiceException("업로드 가능한 파일 용량을 초과하였습니다.");
			}
		}
	}

	private void uploadFile(String id, String path, MultipartFile... files) {
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue;
			}

			try {
				StringBuilder fullPath = new StringBuilder();
				fullPath.append(id);
				
				File newFile = new File(fullPath.toString());
				FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), e);
			}
		}
	}
}