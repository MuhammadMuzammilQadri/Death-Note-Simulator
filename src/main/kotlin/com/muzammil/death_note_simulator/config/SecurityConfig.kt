package com.muzammil.death_note_simulator.config

import com.muzammil.death_note_simulator.filters.JwtRequestFilter
import com.muzammil.death_note_simulator.services.userdetail.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Created by Muzammil on 1/9/21.
 */

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
  
  @Autowired
  lateinit var myUserDetailService: MyUserDetailService
  
  @Autowired
  lateinit var jwtRequestFilter: JwtRequestFilter
  
  override fun configure(auth: AuthenticationManagerBuilder?) {
    auth?.userDetailsService(myUserDetailService)
  }
  
  override fun configure(http: HttpSecurity?) {
    http?.csrf()?.disable()
      ?.authorizeRequests()?.antMatchers("/auth/**")?.permitAll()
      ?.anyRequest()?.authenticated()
      ?.and()?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    
    http?.addFilterBefore(jwtRequestFilter,
                          UsernamePasswordAuthenticationFilter::class.java)
  }
  
  @Bean
  override fun authenticationManagerBean(): AuthenticationManager {
    return super.authenticationManagerBean()
  }
  
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return NoOpPasswordEncoder.getInstance()
  }
}
