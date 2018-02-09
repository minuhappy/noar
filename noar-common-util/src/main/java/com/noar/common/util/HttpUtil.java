/* Copyright © HatioLab Inc. All rights reserved. */
package com.noar.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author Minu.Kim
 */
public class HttpUtil {

	/**
	 * Get방식으로 전송
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static String get(String uri) throws Exception {
		return get(uri, null);
	}

	public static String get(String uri, Map<String, Object> params) throws Exception {
		// URL 주소
		String sUrl = uri;
		if (params != null && !params.isEmpty()) {
			StringBuilder makeParam = new StringBuilder();
			makeParam.append("?");

			Set<String> keys = params.keySet();
			for (String key : keys) {
				makeParam.append("&").append(key).append("=").append(params.get(key));
			}

			sUrl += makeParam.toString().replaceFirst("&", "");
		}

		URL url = new URL(sUrl);
		// URLConnection connection = url.openConnection();
		// connection.setConnectTimeout(1000); // Timeout 설정
		// connection.setReadTimeout(10000); // Read Timeout 설정
		BufferedReader bfReader = new BufferedReader(new InputStreamReader(url.openStream()));

		String value = "";
		StringBuilder response = new StringBuilder();
		while ((value = bfReader.readLine()) != null) {
			response.append(value);
		}

		bfReader.close();
		return response.toString();
	}

	/**
	 * Execute Post Method For Json.
	 * 
	 * @param url
	 * @param inputJson
	 * @return
	 * @throws Exception
	 */
	public static String postByJson(String url, String inputJson) throws Exception {
		return postByJson(url, inputJson, null);
	}

	public static String postByJson(String url, String inputJson, Map<String, String> headerMap) throws Exception {
		assert url == null || url.isEmpty() : "URL is emtpy.";

		HttpPost request = new HttpPost(url);

		// Header 정보 추가
		Map<String, String> map = new HashMap<String, String>();
		map.put("content-type", ContentType.APPLICATION_JSON.toString());

		if (headerMap != null && !headerMap.isEmpty()) {
			map.putAll(headerMap);
		}

		for (String key : map.keySet()) {
			request.addHeader(key, map.get(key));
		}

		request.setEntity(new StringEntity(inputJson, "UTF-8"));

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			HttpEntity entity = client.execute(request).getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
	}

	/**
	 * Post방식으로 전송
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static String post(String uri) throws Exception {
		return post(uri, null);
	}

	public static String post(String uri, Map<String, Object> params) throws Exception {
		return post(uri, params, "UTF-8");
	}

	public static String post(String uri, Map<String, Object> paramMap, String charSet) throws Exception {
		String params = "";

		if (paramMap != null && !paramMap.isEmpty()) {
			StringBuilder makeParam = new StringBuilder();

			Set<String> keys = paramMap.keySet();
			for (String key : keys) {
				Object value = paramMap.get(key);
				if (value == null) {
					continue;
				}

				makeParam.append("&");
				makeParam.append(URLEncoder.encode(key, charSet));
				makeParam.append("=");
				makeParam.append(URLEncoder.encode(value.toString(), charSet));
			}
			params = makeParam.toString().replaceFirst("&", "");
		}

		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		connection.setDoOutput(true);
		connection.setDoInput(true);

		OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
		osw.write(params);
		osw.flush();

		BufferedReader bfReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

		String value = "";
		StringBuilder response = new StringBuilder();
		while ((value = bfReader.readLine()) != null) {
			response.append(value);
		}

		osw.close();
		bfReader.close();
		return response.toString();
	}

	/**
	 * request에서 내용 추출.
	 * 
	 * @param request
	 * @return
	 */
	public static String getInputStream(HttpServletRequest request) throws Exception {
		StringBuilder response = new StringBuilder();
		BufferedReader reader = request.getReader();

		String value = "";
		while ((value = reader.readLine()) != null) {
			response.append(value).append("\n");
		}

		return response.toString();
	}
}