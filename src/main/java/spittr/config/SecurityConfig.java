package spittr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.inMemoryAuthentication() //Enable an in-memory user store
		.withUser("user").password("password").roles("USER").
		and().withUser("admin").password("password").roles("USER","ADMIN");
	}
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {
	http.authorizeRequests()
	.antMatchers("/spitter/me").hasRole("SPITTER")
	.antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER")
	.anyRequest().permitAll()
	.and()
	.requiresChannel()
	.antMatchers("/spitter/form").requiresSecure(); //Require HTTPS
	}

}
