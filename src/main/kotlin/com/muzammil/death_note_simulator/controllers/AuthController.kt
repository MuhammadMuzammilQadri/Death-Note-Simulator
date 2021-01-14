package com.muzammil.death_note_simulator.controllers

import com.muzammil.death_note_simulator.config.JwtUtil
import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.dtos.AuthenticationRequest
import com.muzammil.death_note_simulator.models.dtos.AuthenticationResponse
import com.muzammil.death_note_simulator.services.userdetail.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Muzammil on 1/9/21.
 */
@RestController
@RequestMapping("auth/")
class AuthController {
  
  @Autowired
  lateinit var authenticationManager: AuthenticationManager
  
  @Autowired
  lateinit var userDetailService: MyUserDetailService
  
  @Autowired
  lateinit var jwtUtil: JwtUtil
  
  @PostMapping(value = ["login"])
  fun login(@RequestBody
            body: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
    try {
      return authenticationManager
        .authenticate(UsernamePasswordAuthenticationToken(
          body.username, body.password))
        .principal
        .let {
          when (it) {
            is UserDetails -> {
              jwtUtil.generateToken(it)
            }
            else           -> {
              throw UnknownException("Invalid principal")
            }
          }
        }
        .let { ResponseEntity.ok(AuthenticationResponse(it)) }
      
    } catch (e: Exception) {
      throw Exception("Incorrect username or password", e)
    }
  }
}
