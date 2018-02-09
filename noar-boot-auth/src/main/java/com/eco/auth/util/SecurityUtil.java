package com.eco.auth.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eco.auth.Constants;
import com.eco.auth.security.EcoUserDetails;
import com.eco.auth.security.IUser;
import com.noar.util.BeanUtil;

/**
 * 보안 및 인증 관련 유틸리티 클래스
 * 
 * @author Minu.Kim
 */
public class SecurityUtil {
	/**
	 * 승인 여부 확인.
	 * 
	 * @return
	 */
	public static boolean isAnonymous() {
		Authentication auth = getAuthentication();
		return auth == null || auth instanceof AnonymousAuthenticationToken;
	}

	/**
	 * 인증정보 가져오기 실행.
	 * 
	 * @return
	 */
	public static Authentication getAuthentication() {
		SecurityContext sc = SecurityContextHolder.getContext();
		if (sc == null) {
			sc = (SecurityContext) SessionUtil.getAttribute("SPRING_SECURITY_CONTEXT");
		}
		return sc == null ? null : sc.getAuthentication();
	}

	/**
	 * 인증된 사용자의 정보 가져오기 실행.
	 * 
	 * @return
	 */
	public static IUser getUer() {
		Authentication auth = getAuthentication();
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			return null;
		}

		EcoUserDetails elidomUserDetails = (EcoUserDetails) auth.getPrincipal();
		return elidomUserDetails.getUser();
	}

	/**
	 * 비밀번호 암호화(SHA-256) 실행.
	 * 
	 * @param value
	 * @return
	 */
	public static String encodePassword(String value) {
		return BeanUtil.get(MessageDigestPasswordEncoder.class).encodePassword(value, Constants.PASSWORD_ENCODER_SALT);
	}

	/**
	 * 비밀번호 일치 여부 확인.
	 * 
	 * @param encPass : 암호화된 Pass
	 * @param rawPass : 평문의 Pass
	 * @return
	 */
	public static boolean isPasswordValid(String encPass, String rawPass) {
		return BeanUtil.get(MessageDigestPasswordEncoder.class).isPasswordValid(encPass, rawPass, Constants.PASSWORD_ENCODER_SALT);
	}

	/**
	 * Random Password 생성.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String randomPassword() {
		char[] initRandomChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9' };

		char[] randomChar = new char[6];
		for (int i = 0; i < 6; i++) {
			randomChar[i] += initRandomChar[(int) (Math.random() * initRandomChar.length)];
		}

		StringBuffer buf = new StringBuffer();
		for (char randChar : randomChar) {
			buf.append(randChar);
		}
		return buf.toString();
	}
}