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
                        .requestMatchers("/api/admin/profile/**").hasRole("ADMIN")
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
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // yoki faqat ""
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
