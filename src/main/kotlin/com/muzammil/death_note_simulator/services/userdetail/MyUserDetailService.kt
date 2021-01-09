package com.muzammil.death_note_simulator.services.userdetail

import com.muzammil.death_note_simulator.exceptions.AuthException
import com.muzammil.death_note_simulator.models.MyUserDetails
import com.muzammil.death_note_simulator.repos.person.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * Created by Muzammil on 1/9/21.
 */

@Service
class MyUserDetailService : UserDetailsService {
  
  @Autowired
  lateinit var userRepo: UserRepo
  
  override fun loadUserByUsername(username: String): UserDetails {
    userRepo.findByName(username)?.let {
      return MyUserDetails(it)
    } ?: throw AuthException("Incorrect username or passworddd")
  }
}
