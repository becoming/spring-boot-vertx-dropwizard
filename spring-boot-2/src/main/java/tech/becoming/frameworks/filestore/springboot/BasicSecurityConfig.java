package tech.becoming.frameworks.filestore.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// @Configuration
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.httpBasic();

        http.authorizeRequests().
                antMatchers("/actuator/health").permitAll().
                antMatchers("/swagger").permitAll().

                antMatchers(HttpMethod.GET, "/**").hasAnyRole("USER").

                antMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN").
                antMatchers(HttpMethod.DELETE, "/**").hasAnyRole("ADMIN").
                antMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /**
     * @see <a href="https://stackoverflow.com/a/49445610/1107450">https://stackoverflow.com/a/49445610/1107450</a>
     * @see <a href="https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding">https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding</a>
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        UserDetails user = userBuilder
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = userBuilder
                .username("admin")
                .password("password")
                .roles("USER","ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}