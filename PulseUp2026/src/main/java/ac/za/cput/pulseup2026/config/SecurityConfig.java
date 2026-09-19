package ac.za.cput.pulseup2026.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter
                    jwtAuthenticationConverter,
            CorsConfigurationSource
                    corsConfigurationSource
    ) throws Exception {

        http
                .csrf(csrf ->
                        csrf.disable()
                )

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource
                        )
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy
                                        .STATELESS
                        )
                )

                .authorizeHttpRequests(
                        authorize -> authorize

                                .requestMatchers(
                                        HttpMethod.OPTIONS,
                                        "/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/error"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/auth/login",
                                        "/api/auth/register/student",
                                        "/api/auth/bootstrap-admin"
                                ).permitAll()

                                // Student profile
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/students/me"
                                ).hasRole("STUDENT")

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/students/me/profile"
                                ).hasRole("STUDENT")

                                // Cyngatha voucher routes
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/vouchers/me",
                                        "/api/vouchers/me/progress"
                                ).hasRole("STUDENT")

                                .requestMatchers(
                                        "/api/vouchers/**"
                                ).hasAnyRole(
                                        "STUDENT",
                                        "ADMIN"
                                )

                                // Staff profile
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/staff/me"
                                ).hasRole("STAFF")

                                // Admin registrations
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/auth/register/staff",
                                        "/api/auth/register/admin"
                                ).hasRole("ADMIN")

                                // Admin account management
                                .requestMatchers(
                                        "/api/students/**",
                                        "/api/staff/**",
                                        "/api/admins/**"
                                ).hasRole("ADMIN")

                                // Appointment reads
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/appointments/staff/*"
                                ).hasAnyRole(
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/appointments/student/*",
                                        "/api/appointments/my-queue"
                                ).hasAnyRole(
                                        "STUDENT",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/appointments"
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/appointments/*"
                                ).hasAnyRole(
                                        "STUDENT",
                                        "STAFF",
                                        "ADMIN"
                                )

                                // Appointment creation
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/appointments",
                                        "/api/appointments/**"
                                ).hasRole("STUDENT")

                                // Student/admin changes
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/appointments/*/cancel",
                                        "/api/appointments/*/reschedule"
                                ).hasAnyRole(
                                        "STUDENT",
                                        "ADMIN"
                                )

                                // Staff/admin completion
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/appointments/*/status"
                                ).hasAnyRole(
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/appointments/**"
                                ).hasRole("ADMIN")

                                // Time slots
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/time-slots/staff/*"
                                ).hasAnyRole(
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/time-slots",
                                        "/api/time-slots/*"
                                ).hasAnyRole(
                                        "STUDENT",
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/time-slots",
                                        "/api/time-slots/**"
                                ).hasAnyRole(
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/time-slots/**"
                                ).hasAnyRole(
                                        "STAFF",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/time-slots/**"
                                ).hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )

                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt ->
                                                jwt.jwtAuthenticationConverter(
                                                        jwtAuthenticationConverter
                                                )
                                )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter
    jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter
                authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter
                .setAuthoritiesClaimName(
                        "roles"
                );

        authoritiesConverter
                .setAuthorityPrefix(
                        "ROLE_"
                );

        JwtAuthenticationConverter
                authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter
                .setJwtGrantedAuthoritiesConverter(
                        authoritiesConverter
                );

        return authenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}