package com.mpxds.mpbasic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class MpSecurityConfig extends WebSecurityConfigurerAdapter {
	//
	@Bean
	public MpAppUserDetailsService userDetailsService() {
		//
		return new MpAppUserDetailsService();
	}

	@Configuration
	@Order(1)
	public static class MpApp1ConfigurationAdapter extends WebSecurityConfigurerAdapter {

		public MpApp1ConfigurationAdapter() {
			super();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//
			MpJsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new MpJsfLoginUrlAuthenticationEntryPoint();

			jsfLoginEntry.setLoginFormUrl("/Login");
			jsfLoginEntry.setRedirectStrategy(new MpJsfRedirectStrategy());

			MpJsfAccessDeniedHandler jsfDeniedEntry = new MpJsfAccessDeniedHandler();

			jsfDeniedEntry.setLoginPath("/AcessoNegado");
			jsfDeniedEntry.setContextRelative(true);

			http.csrf()
					.disable().headers().frameOptions().sameOrigin()
					.and()
					//
					.authorizeRequests()
					.antMatchers("/index.html", "/MpLogin.xhtml", "/Login", "/MpErro.xhtml", "/Erro", "/Mp404.xhtml",
							"/Erro404", "/ContatoSite", "/MpSite.xhtml", "/Site", "/bs/**",
						"/MpSiteConstrucao.xhtml", "/SiteConstrucao", 
						"/mpCieloWs/1/sales/", "/json/metallica/", // ApiRest CIELO 3.0 
						"/mpTituloWs", "/mpTituloJsonWs", "/contacts", "/mpBoletoWs", "/mpBoletoJsonWs", // Api Rest Jars ...
//							"/SistemaConfig", "/EmissaoBoleto",
						"/ServicoBoleto", "/MpServicoBoleto.xhtml",
						"/ServicoTitulo", "/MpServicoTitulo.xhtml",
						"/javax.faces.resource/**").permitAll()
					.antMatchers("/MpHome.xhtml", "/Home", "/MpAcessoNegado.xhtml", "/MpLogout.xhtml", "/Logout",
							"/AcessoNegado", "/dialogos/**").authenticated()
					.antMatchers("/mpUser/**").hasAnyRole("USUARIOS", "ADMINISTRADORES")
					.antMatchers("/mpCartorio/**").hasAnyRole("CARTORIOS", "ADMINISTRADORES")
					.antMatchers("/mpAdmin/**", "/relatorios/**").hasAnyRole("ADMINISTRADORES")
					.and()
					//
					.formLogin()
					.loginPage("/Login")
//					.loginProcessingUrl("/admin_login")
					.failureUrl("/Login?invalid=true")
					.and()
					//
					.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/Logout"))
					.and()
					//
					.exceptionHandling()
					.accessDeniedPage("/AcessoNegado")
					.authenticationEntryPoint(jsfLoginEntry)
					.accessDeniedHandler(jsfDeniedEntry);
		}
	}
	
//	@Configuration
//	@Order(2)
//	public static class MpApp2ConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//		public MpApp2ConfigurationAdapter() {
//			super();
//		}
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			//
//			MpJsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new MpJsfLoginUrlAuthenticationEntryPoint();
//
//			jsfLoginEntry.setLoginFormUrl("/ServicoBoleto");
//			jsfLoginEntry.setRedirectStrategy(new MpJsfRedirectStrategy());
//
//			MpJsfAccessDeniedHandler jsfDeniedEntry = new MpJsfAccessDeniedHandler();
//
//			jsfDeniedEntry.setLoginPath("/AcessoNegado");
//			jsfDeniedEntry.setContextRelative(true);
//
//			http.csrf().
//					disable().headers().frameOptions().sameOrigin()
//					.and()
//					//
//					.authorizeRequests()
//					.antMatchers("/index.html", "/MpLogin.xhtml", "/Login", "/MpErro.xhtml", "/Erro", "/Mp404.xhtml",
//							"/Erro404", "/ContatoSite", "/Site", "/SistemaConfig", "/EmissaoBoleto",
//							"/ServicoBoleto", "/MpServicoBoleto.xhtml",
//							"/javax.faces.resource/**").permitAll()
//					.antMatchers("/MpHome.xhtml", "/Home", "/MpAcessoNegado.xhtml",
//							"/AcessoNegado", "/dialogos/**").authenticated()
//					.antMatchers("/mpUser/**").hasAnyRole("USUARIOS", "ADMINISTRADORES")
//					.and()
//					//
//					.formLogin()
//					.loginPage("/ServicoBoleto")
////					.loginProcessingUrl("/user_login")
//					.failureUrl("/ServicoBoleto?invalid=true")
//					.and()
//					//
//					.logout()
//					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//					.and()
//					//
//					.exceptionHandling()
//					.accessDeniedPage("/AcessoNegado")
//					.authenticationEntryPoint(jsfLoginEntry)
//					.accessDeniedHandler(jsfDeniedEntry);
//		}
//	}
//
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		//
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		//
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(new MpCustomPasswordEncoder());
		//
		return authProvider;
	}
	
}