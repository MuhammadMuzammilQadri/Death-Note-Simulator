package com.muzammil.death_note_simulator.controllers

import com.muzammil.death_note_simulator.security.JwtUtil
import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.MyUserDetails
import com.muzammil.death_note_simulator.models.dtos.AuthenticationRequest
import com.muzammil.death_note_simulator.models.dtos.AuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.PermitAll

/**
 * Created by Muzammil on 1/9/21.
 */
@RestController
@RequestMapping("auth/")
class AuthController {
  
  @Autowired
  lateinit var authenticationManager: AuthenticationManager
  
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
            is MyUserDetails -> {
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
