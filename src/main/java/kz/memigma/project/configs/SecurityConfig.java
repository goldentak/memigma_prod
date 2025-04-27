package kz.memigma.project.configs;

import kz.memigma.project.services.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/auth/verify", "/landing", "/auth/verify-code", "/home", "auth/check-username", "api/upload").permitAll()
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/*.css",
                                "/*.js",
                                "/assets/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(
                                "/api/upload",
                                "/auth/**"
                        ).permitAll()
                        .requestMatchers("/api/memes").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new jakarta.servlet.Filter() {
                    @Override
                    public void doFilter(jakarta.servlet.ServletRequest servletRequest,
                                         jakarta.servlet.ServletResponse servletResponse,
                                         jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {
                        HttpServletRequest request = (HttpServletRequest) servletRequest;
                        HttpServletResponse response = (HttpServletResponse) servletResponse;

                        String authHeader = request.getHeader("Authorization");
                        if (authHeader == null) {
                            authHeader = "";
                        }
                        if (!authHeader.isEmpty() && authHeader.startsWith("Bearer ")) {
                            String token = authHeader.substring(7);
                            if (jwtUtil.validateToken(token)) {
                                String username = jwtUtil.extractUsername(token);
                                var auth = new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        List.of(new SimpleGrantedAuthority("USER"))
                                );
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        }
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        System.out.println("SecurityConfig загружен: публичные эндпойнты настроены.");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}