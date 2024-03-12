package fontys.emergencywebapps.configurations.security;


import fontys.emergencywebapps.configurations.security.auth.AuthenticationRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import fontys.emergencywebapps.configurations.security.oauth2.OAuth2AuthenticationFailureHandler;
import fontys.emergencywebapps.configurations.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final OAuth2AuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationEntryPoint authenticationEntryPoint, AuthenticationRequestFilter jwtAuthFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll().requestMatchers(HttpMethod.GET, "/api/auth/registrationConfirm**").permitAll()
                                .requestMatchers("/ws").permitAll().anyRequest().authenticated()
                )
                .oauth2Login(oath2 -> {
                    oath2.loginPage("/oauth2/**").permitAll();
                    oath2.authorizationEndpoint(authorization -> authorization
                            .baseUri("/oauth2/authorize"));
                    oath2.redirectionEndpoint(redirection -> redirection
                            .baseUri("/oauth2/callback/*"));
                    oath2.userInfoEndpoint(userInfo -> userInfo
                            .userAuthoritiesMapper(grantedAuthoritiesMapper()));
                    oath2.successHandler(oAuth2LoginSuccessHandler);
                    oath2.failureHandler(oAuth2AuthenticationFailureHandler);

                })
                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    private GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                GrantedAuthority mappedAuthority;

                if (authority instanceof OidcUserAuthority userAuthority) {
                    mappedAuthority = new OidcUserAuthority(
                            "ROLE_USER", userAuthority.getIdToken(), userAuthority.getUserInfo());
                } else if (authority instanceof OAuth2UserAuthority userAuthority) {
                    mappedAuthority = new OAuth2UserAuthority(
                            "ROLE_USER", userAuthority.getAttributes());
                } else {
                    mappedAuthority = authority;
                }

                mappedAuthorities.add(mappedAuthority);
            });

            return mappedAuthorities;
        };
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:5173");

            }
        };
    }

}
