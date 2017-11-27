/**
 * 
 */
package com.noar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author SK.YOON
 * 
 */
public class Base64Util {

	/**
	 * Base64Encoding 방식으로 바이트 배열을 아스키 문자열로 인코딩한다. In-Binany, Out-Ascii
	 * 
	 * @param encodeBytes
	 *            인코딩할 바이트 배열(byte[])
	 * @return 인코딩된 아스키 문자열(String)
	 * @throws IOException
	 */
	public static String encode(byte[] encodeBytes) throws IOException {
		byte[] buf = null;
		String strResult = null;

		BASE64Encoder base64Encoder = new BASE64Encoder();
		ByteArrayInputStream bin = new ByteArrayInputStream(encodeBytes);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		base64Encoder.encodeBuffer(bin, bout);

		buf = bout.toByteArray();
		strResult = new String(buf).trim();
		return strResult;
	}

	/**
	 * Base64Decoding 방식으로 아스키 문자열을 바이트 배열로 디코딩한다. In-Ascii, Out-Binany
	 * 
	 * @param strDecode
	 *            디코딩할 아스키 문자열(String)
	 * @return 디코딩된 바이트 배열(byte[])
	 * @throws IOException 
	 */
	public static byte[] decode(String strDecode) throws IOException {
		byte[] buf = null;

		BASE64Decoder base64Decoder = new BASE64Decoder();
		ByteArrayInputStream bin = new ByteArrayInputStream(
				strDecode.getBytes());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		base64Decoder.decodeBuffer(bin, bout);

		buf = bout.toByteArray();
		return buf;
	}
}
