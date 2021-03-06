package com.muzammil.death_note_simulator.security

import com.muzammil.death_note_simulator.filters.JwtRequestFilter
import com.muzammil.death_note_simulator.services.userdetail.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
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
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  jsr250Enabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
  
  @Autowired
  lateinit var myUserDetailService: MyUserDetailService
  
  @Autowired
  lateinit var jwtRequestFilter: JwtRequestFilter
  
  override fun configure(auth: AuthenticationManagerBuilder?) {
    auth?.userDetailsService(myUserDetailService)
  }
  
  override fun configure(http: HttpSecurity?) {
    http?.csrf()?.disable()
      ?.addFilterBefore(jwtRequestFilter,
                        UsernamePasswordAuthenticationFilter::class.java)
      ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
