package com.noar.core.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * @author Minu.Kim
 */
public class ServletImpl extends ServletInputStream {
	private InputStream inputStream;

	public ServletImpl(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public int read() throws IOException {
		return this.inputStream.read();
	}
}