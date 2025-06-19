/*
package uz.zafar.onlinecourse.config;


//import com.example.onlinecoursemain.component.jwt.JwtAuthFilter;
//import com.example.onlinecoursemain.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.zafar.onlinecourse.component.jwt.JwtAuthFilter;
import uz.zafar.onlinecourse.service.AuthService;
//import uz.farobiy.lesson_11_backend.component.jwt.JwtAuthFilter;
//import uz.farobiy.lesson_11_backend.service.AuthService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    public AuthService authService;

    // Configuring HttpSecurity
    *//*

 */
/*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/auth/**",
                        "/api/static/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/files/**",
                        "/static/**"
                ).permitAll()
                .requestMatchers("/api/user-info/get-user").hasAnyRole("ADMIN", "TEACHER", "STUDENT", "GUEST")
                .requestMatchers("/api/admin/course/**").hasAnyRole("ADMIN")
                .requestMatchers("/api/admin/group/**").hasAnyRole("ADMIN")
                .requestMatchers("/api/admin/lesson/**").hasAnyRole("ADMIN")
                .and()
//                .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated()
//                .and()
                .authorizeHttpRequests().requestMatchers("/api/admin/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }*//*
 */
/*

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/files/**",
                                "/static/**",
                                "/uploads/**",
                                "/api/auth/**",
                                "/api/static/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/api/user-info/get-user")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "GUEST")

                        .requestMatchers("/api/admin/course/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/group/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/lesson/**").hasRole("ADMIN")

                        .requestMatchers("/api/admin/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
*//*


package uz.zafar.onlinecourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.zafar.onlinecourse.component.jwt.JwtAuthFilter;
import uz.zafar.onlinecourse.service.AuthService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC: images, uploads, swagger
                        .requestMatchers(
                                "/files/**",     // <-- Static files from local disk
                                "/uploads/**",   // <-- Uploaded files
                                "/static/**",
                                "/api/auth/**",
                                "/api/static/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Authenticated endpoints
                        .requestMatchers("/api/user-info/get-user")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "GUEST")
                        .requestMatchers("/api/admin/course/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/group/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/lesson/**").hasRole("ADMIN")

                        // All other admin APIs
                        .requestMatchers("/api/admin/**").authenticated()

                        // Catch all remaining
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true); // <-- Bu muhim JWT uchun

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
*/
package uz.zafar.onlinecourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.zafar.onlinecourse.component.jwt.JwtAuthFilter;
import uz.zafar.onlinecourse.service.AuthService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/files/**",
                                "/files/homework-submission/**",
                                "/static/**",
                                "/uploads/**",
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/user-info/get-user")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "GUEST")
                        .requestMatchers("/api/admin/change-password/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/change-password").hasRole("ADMIN")
                        .requestMatchers("/api/admin/course/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/group/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/lesson/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/review/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/user/**").hasRole("ADMIN")
                        .requestMatchers("/api/teacher/course/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/like/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/comment/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/statistic/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/change-password/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/change-password").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/lesson/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/group/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/homework/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/grade/**").hasRole("TEACHER")
                        .requestMatchers("/api/teacher/homework-submission/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/course/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/lesson/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/review/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/change-password/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/change-password").hasRole("STUDENT")
                        .requestMatchers("/api/student/group/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/grade/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/comment/**").hasRole("STUDENT")
                        .requestMatchers("/api/student/like/**").hasRole("STUDENT")
                        .requestMatchers("/api/file-types/list").hasAnyRole("TEACHER", "STUDENT", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // yoki faqat "http://localhost:3000"
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
