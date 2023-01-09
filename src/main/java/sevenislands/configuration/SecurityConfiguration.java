package sevenislands.configuration;

import java.security.SecureRandom;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/oups").hasAnyAuthority("player", "admin") // .permitAll()
				.antMatchers("/error").hasAnyAuthority("player", "admin")
				.antMatchers(HttpMethod.GET, "/").permitAll()
				.antMatchers("/user/new").permitAll()
				.antMatchers("/welcome").permitAll()
				//.antMatchers("/login").permitAll()
				.antMatchers("/custom-logout").permitAll()
				.antMatchers("/achievements").hasAnyAuthority("player", "admin")
				.antMatchers("/myAchievements").hasAnyAuthority("player")
				.antMatchers("/signup/**").permitAll()
				.antMatchers("/session/**").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/settings").hasAnyAuthority("player", "admin")
				.antMatchers("/players/**").hasAnyAuthority("admin")
				.antMatchers("/home/**").hasAnyAuthority("player", "admin")
				.antMatchers("/lobby/**").hasAnyAuthority("player")
				.antMatchers("/join/**").hasAnyAuthority("player")
				.antMatchers("/game/finished").hasAnyAuthority("admin")
				.antMatchers("/game/InProgress").hasAnyAuthority("admin")
				.antMatchers("/statistics").hasAnyAuthority("player", "admin")
				.antMatchers("/dailyStatistics").hasAnyAuthority("player", "admin")
				.antMatchers("/myStatistics").hasAnyAuthority("player")
				.antMatchers("/controlAchievements/**").hasAnyAuthority("admin")
				.antMatchers("/game/**").hasAnyAuthority("player")
				.antMatchers("/endGame").hasAnyAuthority("player")
				.antMatchers("/turn/**").hasAnyAuthority("player")
				.antMatchers("/controlPanel/**").hasAnyAuthority("admin")
				.antMatchers("/delete/**").hasAnyAuthority("player")
				.antMatchers("/h2-console/**").hasAnyAuthority("admin")
				.antMatchers("/ranking/**").hasAnyAuthority("player", "admin")
				.antMatchers("/friends/**").hasAnyAuthority("player")
				.anyRequest().denyAll()
				.and()
				.formLogin()
				.defaultSuccessUrl("/home")
				/* .loginPage("/login") */
				.failureUrl("/login-error")
				.and()
				.logout()
				.logoutUrl("/custom-logout")
				.logoutSuccessUrl("/");
		// Configuración para que funcione la consola de administración
		// de la BD H2 (deshabilitar las cabeceras de protección contra
		// ataques de tipo csrf y habilitar los framesets si su contenido
		// se sirve desde esta misma página.
		http.csrf().ignoringAntMatchers("/h2-console/**");
		http.headers().frameOptions().sameOrigin();
		http.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(
						"select nickname, password, enabled "
								+ "from user "
								+ "where nickname = ?")
				.authoritiesByUsernameQuery(
						"select nickname, type "
								+ "from user "
								+ "where nickname = ?")
				.passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		//PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();							//Por motivos de testeo tenemos hecho que la
		String str = "seed";																	//semilla que utilice para encriptar las
		byte[] bt = str.getBytes();																//contraseñas sea siempre la misma,
		PasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom(bt));//habria que eliminar el "new SecureRandom(bt)"
		return encoder;
	}

	@Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
	public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
	}
}
