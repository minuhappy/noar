package com.minu.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.minu.core.exception.ServiceException;

public class HttpUtil {
	
	/**
	 * Request에서 파라미터 명이 name인 값을 찾는다. 
	 * 
	 * @param name
	 * @param request
	 * @return
	 */
	public static String[] findParamValue(String name, HttpServletRequest request) {
		String[] values = null;
		
		Iterator<String> keyIter = request.getParameterMap().keySet().iterator();
		while(keyIter.hasNext()) {
			String paramName = keyIter.next();
			if(ValueUtil.isEqual(name, paramName)) {
				values = request.getParameterValues(name);
				break;
			}
		}
		return values;
	}
	
	/**
	 * Request에서 파라미터 명이 name인 값 중 첫번째 값을 찾는다. 
	 * 
	 * @param name
	 * @param request
	 * @return
	 */
	public static String findFirstParamValue(String name, HttpServletRequest request) {
		String[] values = findParamValue(name, request);
		return values != null && values.length >= 1 ? values[0] : null;
	}
	
	/**
	 * Request 모든 Header를 문자열로 리턴 
	 * 
	 * @param request
	 * @return
	 */
	public static String getAllHeader(HttpServletRequest request) {
		StringBuilder str = new StringBuilder();
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String header = headerNames.nextElement();
			str.append(header).append("=").append(request.getHeader(header)).append(File.separator);
		}
		
		return str.toString();
	}
	
	/**
	 * Get방식으로 전송
	 * 
	 * @param uri
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String executeGetMethod(String uri) throws Exception {
		return executeGetMethod(uri, null);
	}

	public static String executeGetMethod(String uri, Map<String, Object> params) throws Exception {
		// URL 주소
		String sUrl = uri;
		if (ValueUtil.isNotEmpty(params)) {
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
	 * @param url
	 * @param inputJson
	 * @return
	 * @throws Exception
	 */
	public static String executePostMethodForJson(String url, String inputJson) throws Exception {
		return executePostMethodForJson(url, inputJson, null);
	}

	public static String executePostMethodForJson(String url, String inputJson, Map<String, String> headerMap) throws Exception {
		if (ValueUtil.isEmpty(url)) {
			throw new ServiceException("URL is Empty.", null);
		}

		HttpPost request = new HttpPost(url);

		// Header 정보 추가
		Map<String, String> map = new HashMap<String, String>();
		map.put("content-type", ContentType.APPLICATION_JSON.toString());

		if (ValueUtil.isNotEmpty(headerMap)) {
			map.putAll(headerMap);
		}

		for (String key : map.keySet()) {
			request.addHeader(key, map.get(key));
		}

		CloseableHttpClient client = HttpClientBuilder.create().build();
		request.setEntity(new StringEntity(inputJson, "UTF-8"));
		return executeSafeRequest(client, request);
	}
	

	/**
	 * Excute Http Request.
	 * @param client
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String executeSafeRequest(CloseableHttpClient client, HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpEntity entity = null;
		String response = "";
		try {
			entity = client.execute(request).getEntity();
			response = EntityUtils.toString(entity, "UTF-8");
		} finally {
			client.close();
		}
		return response;
	}

	/**
	 * Post방식으로 전송
	 * 
	 * @param uri
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String executePostMethod(String uri) throws Exception {
		return executePostMethod(uri, null);
	}

	public static String executePostMethod(String uri, Map<String, Object> params) throws Exception {
		return executePostMethod(uri, params, "UTF-8");
	}

	public static String executePostMethod(String uri, Map<String, Object> paramMap, String charSet) throws Exception {
		String params = "";

		if (ValueUtil.isNotEmpty(paramMap)) {
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
}