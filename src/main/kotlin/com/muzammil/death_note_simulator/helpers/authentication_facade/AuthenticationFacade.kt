package com.muzammil.death_note_simulator.helpers.authentication_facade

import com.muzammil.death_note_simulator.models.MyUserDetails
import com.muzammil.death_note_simulator.models.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade : IAuthenticationFacade {
  
  override fun getUser(): User {
    return (SecurityContextHolder.getContext()
      .authentication
      .principal as MyUserDetails).user
  }
}
