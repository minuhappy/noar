package com.eco.auth.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.stereotype.Service;

import com.eco.auth.Constants;
import com.eco.auth.security.IUser;
import com.eco.auth.security.IUserService;
import com.eco.auth.util.SecurityUtil;
import com.eco.auth.util.SessionUtil;
import com.noar.util.BeanUtil;
import com.noar.util.JsonUtil;
import com.noar.util.ValueUtil;

@Service
public class AuthenticationFilter extends AbstractSecurityWebApplicationInitializer implements Filter {
	private static final String APPNAME_LOGOUT_URL = "/auth/logout";

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String appName = this.toAppNameByRequestUri(request);
		if (APPNAME_LOGOUT_URL.equals(appName)) {
			this.logout(request);
			return;
		}

		/**
		 * 인증된 사용자일 경우, 인증을 실행하지 않음.
		 */
		if (!SecurityUtil.isAnonymous()) {
			chain.doFilter(req, res);
			return;
		}

		try {
			String authType = request.getHeader(Constants.AUTH_TYPE);

			// 기본 인증.
			if (ValueUtil.isEmpty(authType)) {
				String userId = request.getHeader(Constants.PARAMNAME_USERNAME);
				String userPwd = request.getHeader(Constants.PARAMNAME_PASSWORD);

				this.doAuthenticate(userId, userPwd);
			} else {
				// HttpSender를 통한 JSON 호출 시, 인증 실행.
				String authKey = request.getHeader(Constants.AUTH_KEY);

				/**
				 * Type에 따른 인증 실행
				 */

				switch (authType) {
				case Constants.AUTH_TYPE_JSON :
					this.doJsonAuth(authKey, request, response);
					break;

				case Constants.AUTH_TYPE_TOKEN :
					this.doTokenAuth(authKey);
					break;
				default :
					throw new AccessDeniedException("Invalid Auth Type.[" + authType + "]");
				}
			}
		} catch (Exception e) {
			this.processUnauthorized(request, response);
			return;
		}

		chain.doFilter(req, res);
	}

	/**
	 * 인증이 안 된 경우 처리
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void processUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setCharacterEncoding(Constants.CHAR_SET_UTF8);
		response.setContentType(Constants.CONTENT_TYPE_JSON_UTF_8);
	}

	public Authentication doAuthenticate(String id, String password) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);

		List<AuthenticationProvider> list = new ArrayList<AuthenticationProvider>();
		list.add(daoAuthenticationProvider);

		ProviderManager providerManager = new ProviderManager(list);
		Authentication authResult = null;

		try {
			// 비밀번호 암호화를 하지 않고 인증 실행.
			authResult = providerManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));
		} catch (AuthenticationException ae) {
			// 비밀번호를 암호화 처리하여 인증 실행.
			String encodePass = SecurityUtil.encodePassword(password);
			authResult = providerManager.authenticate(new UsernamePasswordAuthenticationToken(id, encodePass));
		}

		// 인증정보 Context에 저장.
		SecurityContextHolder.getContext().setAuthentication(authResult);

		return authResult;
	}

	/**
	 * Json 호출에 대한 인증
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private void doJsonAuth(String authKey, HttpServletRequest request, HttpServletResponse response) {
		String authJsonValue = new String(Base64.getDecoder().decode(authKey.getBytes()));
		IUser user = JsonUtil.underScoreJsonToObject(authJsonValue, IUser.class);

		String userId = user.getUsername();
		String password = user.getPassword();

		Authentication authResult = this.doAuthenticate(userId, password);

		SecurityContextHolder.getContext().setAuthentication(authResult);
		SessionUtil.setAttribute(Constants.AUTH_TYPE, Constants.AUTH_TYPE_JSON);
	}

	/**
	 * Token 방식에 대한 인증
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private void doTokenAuth(String authKey) {
		IUser user = BeanUtil.get(IUserService.class).getUserByToken(authKey);
		if (ValueUtil.isNotEmpty(user)) {
			Authentication authResult = this.doAuthenticate(user.getUsername(), user.getPassword());
			// 인증정보 Context에 저장.
			SecurityContextHolder.getContext().setAuthentication(authResult);
			SessionUtil.setAttribute(Constants.AUTH_TYPE, Constants.AUTH_TYPE_TOKEN);
		}
	}

	private String toAppNameByRequestUri(HttpServletRequest request) {
		String appName = request.getRequestURI();

		int pathParamIndex = appName.indexOf(';');
		if (pathParamIndex > 0) {
			// strip everything from the first semi-colon
			appName = appName.substring(0, pathParamIndex);
		}

		int queryParamIndex = appName.indexOf('?');
		if (queryParamIndex > 0) {
			// strip everything from the first question mark
			appName = appName.substring(0, queryParamIndex);
		}

		appName = appName.replaceFirst(request.getContextPath(), "");

		return appName;
	}

	/**
	 * Logout
	 * 후처리 작업은 LogoutSuccessHandler 인터페이스를 이용하여 구현.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	private boolean logout(HttpServletRequest req) {
		SecurityContextHolder.clearContext();

		if (req != null) {
			HttpSession session = req.getSession();
			if (session != null) {
				session.invalidate();
			}
		}
		return true;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}