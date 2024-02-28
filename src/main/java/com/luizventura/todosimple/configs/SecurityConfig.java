package com.luizventura.todosimple.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity //So that the class can enable the websecurity system
@EnableGlobalMethodSecurity(prePostEnabled = true) // to garantee a security pre post
public class SecurityConfig {
    
    private static final String[] PUBLIC_MATCHERS = {
        "/" //route PUBLIC for everything get, , put, update, post etc...
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        "/user", //so that the entity user can create the itself
        "/login" //abstract from spring, we don't have to create it
    };

    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        
        http.cors().and().csrf().disable(); //para não atrapalhar nos testes. Só utilizada por aplicações de microserviços mais complexas. Protege quando salva a sessão do lado do servidor (não do cliente).

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll() //for tests  
            .anyRequest().authenticated(); //for any other request, don't allow without authentication
            //.and();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //don't save ession

        return http.build();
    }

    //Padrão - To garantee that there's no "cors" problem:
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
            configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration); //method cors configuration passing our configuration
            return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder(); //to encrypt password when user create passowrd and decrypt when user login to see if password matches the one in the database
    }
}
