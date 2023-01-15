package com.management.auth.security;

import com.management.auth.security.config.*;
import com.management.auth.security.helpers.AppAuthenticationSuccessHandler;
import com.management.auth.service.impl.UserServiceImpl;
import com.management.jwt.AuthEntryPointJwt;
import com.management.jwt.AuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Configuration
    @Order(1)
    static class FormLoginWebSecurityConfigurerAdapter {
        private final UserServiceImpl userService;
        private final AuthEntryPointJwt unauthorizedHandler;
        private final PasswordEncoderBean passwordEncoderBean;

        AuthenticationManager authenticationManager;

        UserDetailsService userDetailsService;

        FormLoginWebSecurityConfigurerAdapter(UserServiceImpl userService, AuthEntryPointJwt unauthorizedHandler, PasswordEncoderBean passwordEncoderBean, UserDetailsService userDetailsService) {
            this.userService = userService;
            this.unauthorizedHandler = unauthorizedHandler;
            this.passwordEncoderBean = passwordEncoderBean;
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManager = authenticationManagerBuilder.build();
            http
                    .cors().and()
                    .csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests().antMatchers("/api/v1/admin/login").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/api/v1/admin/**").hasAuthority("MANAGER")
                    .and()
                    .authenticationManager(authenticationManager)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .anyRequest().permitAll();
            http.authenticationProvider(authenticationProvider());

            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public AuthenticationSuccessHandler appAuthenticationSuccessHandler() {
            return new AppAuthenticationSuccessHandler();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
            return source;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoderBean);

            return authProvider;
        }

        @Bean
        public AuthTokenFilter authenticationJwtTokenFilter() {
            return new AuthTokenFilter();
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}