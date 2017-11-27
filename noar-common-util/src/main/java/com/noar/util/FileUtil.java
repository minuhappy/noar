/**
 * 
 */
package com.minu.base.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;





/**
 * @author Minu
 *
 */
public class FileUtil {
	/**
	 * File -> Byte ��ȯ
	 */
	public String fileToByteString(String filePath) throws Exception {
		FileInputStream fis = new FileInputStream("C:\\fileTest\\eclipse.exe");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];

		int readInt = 0;
		while ((readInt = fis.read(buff)) != -1) {
			baos.write(buff, 0, readInt);
		}

		byte[] arr = baos.toByteArray();
		String encodeStr = Base64Util.encode(arr);
		return encodeStr;
	}

	/**
	 * Byte -> File ��ȯ
	 */
	public void byteToFile(String byteData, String savePath) throws Exception {
		if(ValueUtil.isEmpty(savePath)){
			return;
		}
		
		byte[] data = Base64Util.decode(byteData);
		FileOutputStream out = new FileOutputStream(new File("C:\\fileTest\\eclipse2.exe"));
		out.write(data, 0, data.length);
		out.close();
	}
}
