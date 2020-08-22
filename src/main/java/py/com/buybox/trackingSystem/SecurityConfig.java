package py.com.buybox.trackingSystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import py.com.buybox.trackingSystem.security.JwtFilter;
import py.com.buybox.trackingSystem.security.LoginFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public JwtFilter jwtAuthorizationFilterBean() {
        return new JwtFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/login", "/**/register").permitAll() //permitimos el acceso a /login a cualquiera
                .anyRequest().authenticated() //cualquier otra peticion requiere autenticacion
                .and()
                // Las peticiones /login pasaran previamente por este filtro
                .addFilterBefore(new LoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)

                // Las demás peticiones pasarán por este filtro para validar el token
                .addFilterBefore(jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Creamos una cuenta de usuario por default
        String adminPassword = passwordEncoder().encode("admin");
        String empleadoPassword = passwordEncoder().encode("empleado");
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(adminPassword)
                .roles("ADMIN")
                .and()
                .withUser("empleado")
                .password(empleadoPassword)
                .roles("USER");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
