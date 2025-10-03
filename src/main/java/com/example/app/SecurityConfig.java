package com.example.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private static final String PRODUCT_SERVICE_URL = "/productmicroservice/api/products";
	private static final String ORDER_SERVICE_URL = "/ordermicroservice/api/orders";
	private static final String USER_SERVICE_URL = "/usermicroservice/api/users";

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
        .authorizeExchange(
                exchange -> exchange
                		.pathMatchers(PRODUCT_SERVICE_URL).permitAll()
                		.pathMatchers(PRODUCT_SERVICE_URL + "/**", ORDER_SERVICE_URL + "/**", USER_SERVICE_URL + "/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
        ).httpBasic();
		
		return http.build();
	}
	
	
	@Bean
	public ReactiveUserDetailsService setUserRoles() {
		var user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("user123")
				.roles("USER")
				.build();
		
		var admin = User.withDefaultPasswordEncoder()
				.username("admin")
				.password("admin123")
				.roles("ADMIN")
				.build();
		
		var seller = User.withDefaultPasswordEncoder()
				.username("seller")
				.password("seller123")
				.roles("SELLER")
				.build();
		
		return new MapReactiveUserDetailsService(user, admin, seller);
	}
	
}

	
//	private static final String PRODUCT_SERVICE_URL = "/productmicroservice/api/products";
//	private static final String ORDER_SERVICE_URL = "";
//	private static final String USER_SERVICE_URL = "";
//	
//	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(
//                auth -> auth
//                        .requestMatchers("/fallback/**").permitAll()
//                        .requestMatchers(PRODUCT_SERVICE_URL).permitAll()
////                        .requestMatchers(new RegexRequestMatcher(PRODUCT_SERVICE_URL + "/\\d+", null)).permitAll()
//        ).httpBasic();
//        
//        return httpSecurity.build();
//	}
