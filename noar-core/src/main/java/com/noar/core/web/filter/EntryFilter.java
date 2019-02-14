package com.noar.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.noar.common.util.ThreadPropertyUtil;
import com.noar.core.Constants;
import com.noar.core.web.HttpRequestWrapper;

@Order(1)
@Service
public class EntryFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			ThreadPropertyUtil.doScope(() -> {
				HttpServletRequest request = new HttpRequestWrapper((HttpServletRequest) req);
				ThreadPropertyUtil.put(Constants.HTTP_REQUEST, request);

				chain.doFilter(request, res);
				return null;
			});
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (Throwable t) {
			throw new ServletException(t);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}