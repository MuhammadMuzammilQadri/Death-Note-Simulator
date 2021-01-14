package com.muzammil.death_note_simulator.models

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Created by Muzammil on 1/9/21.
 */
class MyUserDetails(var user: User) : UserDetails {
  
  override fun getAuthorities(): List<SimpleGrantedAuthority>? {
    return mutableListOf(SimpleGrantedAuthority(user.roles.name))
  }
  
  override fun isEnabled(): Boolean {
    return true
  }
  
  override fun getUsername(): String {
    return user.name ?: throw Exception("Bad user name")
  }
  
  override fun isCredentialsNonExpired(): Boolean {
    return true
  }
  
  override fun getPassword(): String {
    return user.password
  }
  
  override fun isAccountNonExpired(): Boolean {
    return true
  }
  
  override fun isAccountNonLocked(): Boolean {
    return true
  }
}
