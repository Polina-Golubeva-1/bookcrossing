package bookcrossing.security;

import bookcrossing.security.filter.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(new AntPathRequestMatcher("/person")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_rent", "GET")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_rent/rent", "POST")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_rent/cancel/{id}", "DELETE")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_borrowal/borrow", "POST")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_borrowal/return", "POST")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/book_borrowal", "GET")).hasAnyRole("ADMIN", "USER")
                                .requestMatchers(new AntPathRequestMatcher("/book_borrowal/delete/{id}", "DELETE")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/black_list", "GET")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/black_list", "POST")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/black_list", "DELETE")).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/security/registration", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/security", "POST")).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
