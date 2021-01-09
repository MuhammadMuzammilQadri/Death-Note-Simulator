package com.muzammil.death_note_simulator.services.userdetail

import com.muzammil.death_note_simulator.models.AppRole
import com.muzammil.death_note_simulator.models.MyUserDetails
import com.muzammil.death_note_simulator.models.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * Created by Muzammil on 1/9/21.
 */

@Service
class MyUserDetailService : UserDetailsService {
  override fun loadUserByUsername(username: String): UserDetails {
    return MyUserDetails(User(username, "123", true, listOf(AppRole.PERSON)))
  }
}
