package pl.owolny.identityprovider.config.security;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.owolny.identityprovider.domain.auth.oauth2user.CustomOAuth2UserService;
import pl.owolny.identityprovider.domain.auth.oidcuser.CustomOidcUserService;
import pl.owolny.identityprovider.domain.auth.user.CustomUserDetailsService;
import pl.owolny.identityprovider.domain.user.UserService;
import pl.owolny.identityprovider.federation.FederatedIdentityAuthenticationSuccessHandler;
import pl.owolny.identityprovider.federation.FederatedIdentityFailureHandler;
import pl.owolny.identityprovider.federation.OAuth2UserHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final FederatedIdentityFailureHandler authenticationFailureHandler;

    public SecurityConfig(UserService userService, CustomOidcUserService customOidcUserService, CustomOAuth2UserService customOAuth2UserService, CustomUserDetailsService customUserDetailsService, FederatedIdentityFailureHandler authenticationFailureHandler) {
        this.userService = userService;
        this.customOidcUserService = customOidcUserService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize ->
                                authorize
//                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/css/**", "/images/**").permitAll()
                                        .requestMatchers("/webjars/**").permitAll()
                                        .requestMatchers("/actuator/**").permitAll()
                                        .requestMatchers("/register").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .requestMatchers("/test").permitAll()
                                        .requestMatchers("/test3").permitAll()
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/logout").permitAll()
                                        .requestMatchers("/link-accounts/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler)
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .oidcUserService(this.customOidcUserService)
                                                .userService(this.customOAuth2UserService)
                                )
                )
                .authenticationProvider(authenticationProvider())
                .build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    OAuth2UserHandler userRepositoryOAuth2UserHandler() {
        return new OAuth2UserHandler(this.userService);
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler = new FederatedIdentityAuthenticationSuccessHandler();
        federatedIdentityAuthenticationSuccessHandler.setOAuth2UserHandler(userRepositoryOAuth2UserHandler());
        return federatedIdentityAuthenticationSuccessHandler;
    }
}
