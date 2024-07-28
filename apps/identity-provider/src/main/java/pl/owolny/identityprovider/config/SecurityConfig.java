package pl.owolny.identityprovider.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.owolny.identityprovider.federation.FederatedIdentityAuthenticationSuccessHandler;
import pl.owolny.identityprovider.federation.UserRepositoryOAuth2UserHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/register-success").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/test3").permitAll()
                                .requestMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .successHandler(authenticationSuccessHandler())
                )
                .oauth2Client(withDefaults());

        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler = new FederatedIdentityAuthenticationSuccessHandler();
        federatedIdentityAuthenticationSuccessHandler.setOAuth2UserHandler(userRepositoryOAuth2UserHandler());
        return federatedIdentityAuthenticationSuccessHandler;
    }

    @Bean
    public UserRepositoryOAuth2UserHandler userRepositoryOAuth2UserHandler() {
        return new UserRepositoryOAuth2UserHandler();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("test")
                .password("test")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
