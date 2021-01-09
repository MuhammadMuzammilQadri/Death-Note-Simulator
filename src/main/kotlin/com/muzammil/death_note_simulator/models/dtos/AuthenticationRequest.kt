package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 1/9/21.
 */
class AuthenticationRequest(var username: String,
                            var password: String) {
  constructor() : this("", "") {
  
  }
}
