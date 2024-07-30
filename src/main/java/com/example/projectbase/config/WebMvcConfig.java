package com.example.projectbase.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.projectbase.base.MyAccessDeniedHandler;
import com.example.projectbase.security.CustomAuthenticationFailureHandler;
import com.example.projectbase.security.CustomAuthenticationSuccessHandler;
import com.example.projectbase.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsService;

	@Bean
	public MyAccessDeniedHandler accessDeniedHandler(){
	    return new MyAccessDeniedHandler();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		//Tất cả có thể truy cập
				.authorizeRequests()
				.antMatchers(
						"/uploads/**",
						"/error/**",
						"/admin/**",
						"/client/**",
                        "/client/assets",
						"/auth/**",
						"/car/layout/**",
						"/car/auth/**",
						"/car/home/**",
						"/car/about/**",
						"/car/client/**",
						"/car/contact/**",
						"/car/product-detail/**",
						"/car/cart/**",
						"/car/account/**",
						"/car/check-out/**",
						"/car/like/**",
						"/car/order/**")
				.permitAll().and()

		//Chỉ có user mới có thể truy cập
				.exceptionHandling()
                .accessDeniedPage("/error/car/404")
                .and()
				.authorizeRequests()
				.antMatchers()
				.access("hasRole('ROLE_USER')").and()
		//Chỉ có admin mới có thể truy cập
				.exceptionHandling()
                .accessDeniedPage("/error/admin/404")
                .and()
				.authorizeRequests()
				.antMatchers(
						"/home/**",
						"/users/**",
						"/categories/**",
						"/brands/**",
						"/products/**",
						"/orders/**",
						"/statistics/**")
				.access("hasRole('ROLE_ADMIN')")
				.anyRequest().authenticated();
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		http.authenticationProvider(authenticationProvider());
		return http.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "uploads";
		Path uploadDir = Paths.get(dirName);
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		if (dirName.startsWith("../"))
			dirName = dirName.replace("../", "");
		registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
}

