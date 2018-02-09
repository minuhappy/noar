package com.noar.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

public class Base64Util {

	/**
	 * Base64 Encode
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] encode(byte[] value) {
		return Base64.getEncoder().encode(value);
	}

	/**
	 * Base64 Encode
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] encode(String value) {
		return Base64.getEncoder().encode(value.getBytes());
	}

	/**
	 * Base64 Encode To String
	 * 
	 * @param value
	 * @return
	 */
	public static String encodeToString(String value) {
		return encodeToString(value.getBytes());
	}

	/**
	 * Base64 Encode To String
	 * 
	 * @param value
	 * @return
	 */
	public static String encodeToString(byte[] value) {
		return Base64.getEncoder().encodeToString(value);
	}

	/**
	 * Base64 Decode
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] decode(byte[] value) {
		return Base64.getDecoder().decode(value);
	}

	/**
	 * Base64 Decode
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] decode(String value) {
		return Base64.getDecoder().decode(value);
	}

	/**
	 * Base64 Decode To String
	 * 
	 * @param value
	 * @return
	 */
	public static String decodeToString(byte[] value) {
		return new String(Base64.getDecoder().decode(value));
	}

	/**
	 * Base64 Decode To String
	 * 
	 * @param value
	 * @return
	 */
	public static String decodeToString(String value) {
		return new String(Base64.getDecoder().decode(value));
	}

	/**
	 * Base64 Decode To InputStream
	 * 
	 * @param value
	 * @return
	 */
	public static InputStream base64DecodeToInputStream(byte[] value) {
		return new ByteArrayInputStream(Base64.getDecoder().decode(value));
	}

	/**
	 * Base64 Decode To InputStream
	 * 
	 * @param value
	 * @return
	 */
	public static InputStream decodeToInputStream(String value) {
		return new ByteArrayInputStream(Base64.getDecoder().decode(value));
	}
}