package com.muzammil.death_note_simulator.models

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Created by Muzammil on 1/9/21.
 */
class MyUserDetails(var user: User?) : UserDetails {
  
  constructor() : this(null)
  
  override fun getAuthorities(): List<SimpleGrantedAuthority>? {
    return user?.roles?.map { SimpleGrantedAuthority(it.name) }
  }
  
  override fun isEnabled(): Boolean {
    return user?.isEnabled!!
  }
  
  override fun getUsername(): String {
    return user?.userName!!
  }
  
  override fun isCredentialsNonExpired(): Boolean {
    return true
  }
  
  override fun getPassword(): String {
    return user?.password!!
  }
  
  override fun isAccountNonExpired(): Boolean {
    return true
  }
  
  override fun isAccountNonLocked(): Boolean {
    return true
  }
}
