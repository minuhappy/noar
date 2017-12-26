package com.noar.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Minu
 */
public class FileUtil {
	private static String CHAR_SET_UTF8 = "UTF-8";

	/**
	 * File 생성.
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param content
	 */
	public static void create(String dirPath, String fileName, String content) {
		// 1. Directory 체크 후 존재하지 않으면 생성
		File dir = new File(dirPath);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 2. File 구분자 추가.
		if (!dirPath.endsWith(File.separator)) {
			dirPath += File.separator;
		}

		File file = new File(dirPath, fileName);
		try (FileWriter fileWriter = new FileWriter(file); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			bufferedWriter.write(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * File 내용 추가
	 * 
	 * @param file
	 * @param value
	 * @throws Exception
	 */
	public static void update(String filePath, String value) {
		update(new File(filePath), value);
	}

	public static void update(File file, String value) {
		String readData = read(file);

		StringJoiner contents = new StringJoiner("\n");
		if (readData != null && !readData.isEmpty()) {
			contents.add(readData);
		}

		contents.add(value);

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
			bufferedWriter.write(contents.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(String filePath) {
		this.delete(new File(filePath));
	}

	public void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files)
				this.delete(f);
		}

		file.delete();
	}

	/**
	 * File 읽어서 내용을 String으로 리턴
	 * 
	 * @param dirPath
	 * @param fileName
	 * @return
	 */
	public static String read(String filePath) {
		return read(new File(filePath));
	}

	public static String read(String dirPath, String fileName) {
		return read(new File(dirPath, fileName));
	}

	public static String read(File file) {
		if (!file.exists())
			return null;
		try {
			return new String(Files.readAllBytes(Paths.get(file.toString())), CHAR_SET_UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * classpath 내의 파일 내용 불러오기.
	 *
	 * @param path
	 * @return
	 */
	public static String readByClassPath(String path) {
		if (path == null || path.isEmpty())
			return null;

		try {
			return IOUtils.toString(new ClassPathResource(path.trim()).getInputStream(), CHAR_SET_UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}