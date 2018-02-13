package com.noar.webapp.sample.service.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaApiSampleBiz {
	public String fileList(String value) {
		String path = System.getProperty("user.dir");
		File file = new File(path);
		subList(file);
		return "Test";
	}

	List<File> fileList = new ArrayList<File>();

	private void subList(File file) {
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File f : list)
				subList(f);
		} else {
			fileList.add(file);
		}
	}
}