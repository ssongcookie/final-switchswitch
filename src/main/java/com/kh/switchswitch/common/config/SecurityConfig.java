package com.kh.switchswitch.common.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.kh.switchswitch.common.security.CustomFailureHandler;
import com.kh.switchswitch.common.security.CustomSuccessHandler;
import com.kh.switchswitch.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final DataSource dataSource;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final CustomSuccessHandler customSuccessHandler;
	private final CustomFailureHandler customFailureHandler;
	
	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	    StrictHttpFirewall firewall = new StrictHttpFirewall();
	    firewall.setAllowSemicolon(true);  
	    return firewall;
	}

	//authenticationManager bean 등록
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	//remember-me 기능
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.mvcMatchers("/switchswitch/resources/**", "/resources/**");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
			.mvcMatchers("/alarm/**","/mypage/**","/member/logout","/exchange/**", "/card/**","/chat/**").hasAnyAuthority("B","C")
			.mvcMatchers("/board/**","/notice/**","/inquiry/**","/top/**","/point/**","/freeSharing/**").hasAnyAuthority("B","C")
			.mvcMatchers("/notice/notice-form","/notice/notice-modify","/top/top-form","/top/top-modify").hasAuthority("C")
			.mvcMatchers("/admin/**").hasAuthority("C")
			.anyRequest().permitAll();
		
		http.formLogin()
			.loginProcessingUrl("/member/login")
			.usernameParameter("memberEmail")
			.loginPage("/member/login")
			.successHandler(customSuccessHandler)
			.failureHandler(customFailureHandler);
		
		http.logout()
		.logoutUrl("/member/logout")
		.logoutSuccessUrl("/member/login");
		
		//remember-me 기능
		http.rememberMe()
			.userDetailsService(memberService)
			.tokenRepository(tokenRepository());
				
		//동시 로그인 차단
		http.sessionManagement()
		.sessionFixation().migrateSession()
		//.invalidSessionUrl("/member/login")			//세션이 유효하지 않을 때 이동할 URL
		.maximumSessions(1)								//최대 허용 가능 세션 수
		//.maxSessionsPreventsLogin(true) 				//false : 기존 세션 만료(defualt)
        .expiredUrl("/member/login?session=expired");	//세션이 만료된 경우 이동할 URL

		http.exceptionHandling().accessDeniedPage("/member/login");
		
		
		http.csrf().ignoringAntMatchers("/mail");
		http.csrf().ignoringAntMatchers("/member/addrPopup");
		http.csrf().ignoringAntMatchers("/market/**");
		http.csrf().ignoringAntMatchers("/point/**");
		http.csrf().ignoringAntMatchers("/board/**");
		http.csrf().ignoringAntMatchers("/admin/**");
		
		http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
//		http.csrf().disable();
	}
	
	 @Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	   auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
	 }

}