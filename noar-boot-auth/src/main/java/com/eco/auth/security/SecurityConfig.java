package com.eco.auth.security;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.eco.auth.Constants;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private Environment env;
	
	/**
	 * 암호화 Algorithm 설정
	 * 
	 * @return
	 */
	@Bean
	public MessageDigestPasswordEncoder messageDigestPasswordEncoder() {
		return new MessageDigestPasswordEncoder(Constants.PASSWORD_ENCODER_ALGORITHM_SHA256);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated()
		// .and()
		// .formLogin()
		// .loginProcessingUrl("/login")
		// .usernameParameter("email")
		// .passwordParameter("password")
		// .successHandler(loginSuccessHandler())
		// .failureHandler(loginFailureHandler())
		// .and()
		// .logout()
		// .logoutUrl("/logout")
		// .logoutSuccessHandler(logoutSuccessHandler())
        .and()
            .authorizeRequests().anyRequest().authenticated()
        .and()
        	.sessionManagement()
        .and()
			.securityContext()
		.and()
        	.csrf().disable();
		
		http.authorizeRequests()
		.and()
        	.headers().frameOptions().disable();
		
        //.anonymous().disable();
	}
	
	// @Override
	// public void configure(AuthenticationManagerBuilder auth) throws Exception
	// {
	// auth.userDetailsService(userDetailsService).passwordEncoder(messageDigestPasswordEncoder);
	// }
	
	/*private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(
					HttpServletRequest request, 
					HttpServletResponse response, 
					FilterChain filterChain)
					throws ServletException, IOException {
				
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	private AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandler() {
			@Override
			public void handle(
					HttpServletRequest httpServletRequest, 
					HttpServletResponse httpServletResponse, 
					AccessDeniedException e)
					throws IOException, ServletException {
				httpServletResponse.getWriter().append("Access denied");
				httpServletResponse.setStatus(403);
			}
		};
	}

	private AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(
					HttpServletRequest httpServletRequest, 
					HttpServletResponse httpServletResponse, 
					AuthenticationException e)
					throws IOException, ServletException {
				httpServletResponse.getWriter().append("Not authenticated");
				httpServletResponse.setStatus(401);
			}
		};
	}*/
	
}