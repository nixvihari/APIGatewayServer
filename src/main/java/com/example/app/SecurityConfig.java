package com.example.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        		//users can access get products and orders only
                                .pathMatchers(PRODUCT_SERVICE_URL,
                                				ORDER_SERVICE_URL).hasRole("PUBLIC")
                                //only PRODUCT_MODIFIER(seller, admin) can add, update or delete products
                                .pathMatchers(HttpMethod.GET, PRODUCT_SERVICE_URL+"/**").hasRole("PUBLIC")
                                .pathMatchers(HttpMethod.POST, PRODUCT_SERVICE_URL+"/**").hasRole("PRODUCT_MODIFIER")
                                .pathMatchers(HttpMethod.PUT, PRODUCT_SERVICE_URL+"/**").hasRole("PRODUCT_MODIFIER")
                                .pathMatchers(HttpMethod.DELETE, PRODUCT_SERVICE_URL+"/**").hasRole("PRODUCT_MODIFIER")
                                //only ORDER_CREATOR(seller, admin) can create orders
                                //only admin can modify or delete orders
                                .pathMatchers(HttpMethod.GET, ORDER_SERVICE_URL +"/**").hasRole("PUBLIC")
                                .pathMatchers(HttpMethod.POST, ORDER_SERVICE_URL +"/**").hasRole("ORDER_CREATOR")
                                .pathMatchers(HttpMethod.PUT, ORDER_SERVICE_URL +"/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.DELETE, ORDER_SERVICE_URL +"/**").hasRole("ADMIN")
                                //only admin can access users API
                                .pathMatchers(USER_SERVICE_URL).hasRole("ADMIN")
                                .pathMatchers(HttpMethod.POST, USER_SERVICE_URL +"/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.PUT, USER_SERVICE_URL +"/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.DELETE, USER_SERVICE_URL +"/**").hasRole("ADMIN")
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
				.roles("USER","PUBLIC")
				.build();
		
		var seller = User.withDefaultPasswordEncoder()
				.username("seller")
				.password("seller123")
				.roles("SELLER", "PUBLIC", "PRODUCT_MODIFIER", "ORDER_CREATOR")
				.build();
		
		var admin = User.withDefaultPasswordEncoder()
				.username("admin")
				.password("admin123")
				.roles("ADMIN", "PUBLIC", "PRODUCT_MODIFIER", "ORDER_CREATOR")
				.build();
		
		return new MapReactiveUserDetailsService(user, admin, seller);
	}
	
}

