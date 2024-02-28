package com.sgurin.inetback.config.security.config;

import com.sgurin.inetback.security.CustomUserDetailsService;
import com.sgurin.inetback.security.RestAuthenticationEntryPoint;
import com.sgurin.inetback.security.TokenAuthenticationFilter;
import com.sgurin.inetback.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.sgurin.inetback.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.sgurin.inetback.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.sgurin.inetback.service.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
//@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService,
                                CustomOAuth2UserService customOAuth2UserService,
                                OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors()
                        .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .csrf()
                        .disable()
                    .httpBasic()
                        .disable()
                    .authorizeRequests()
                        .antMatchers("/auth/login", "/login", "/login2", "/signup", "/actuator/**", "/test/**", "/api/qrcode/**")
                            .permitAll()
                        //.antMatchers("/products").hasAuthority("ROLE_ADMIN")
                        .antMatchers("/auth/**", "/oauth2/**")
                            .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
//                .requiresChannel()
//                    .anyRequest()
//                    .requiresSecure()
//                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                        .and()
//                        .tokenEndpoint()
//                        .accessTokenResponseClient(accessTokenResposeClient())
//                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResposeClient() {
//        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
//        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
//        HttpClient requestFactory = HttpClientBuilder.create().build();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(requestFactory));
//        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//        client.setRestOperations(restTemplate);
//        return client;
//    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(username -> {
//            User user = userService.findByEmail(username);
//
//            if (Objects.isNull(user)) {
//                throw new UsernameNotFoundException("User " + username + " not found.");
//            }
//
//            return user;
//        });
//    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
