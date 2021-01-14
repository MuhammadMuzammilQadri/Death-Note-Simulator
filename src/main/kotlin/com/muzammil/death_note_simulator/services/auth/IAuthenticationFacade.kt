package com.muzammil.death_note_simulator.services.auth

import com.muzammil.death_note_simulator.models.User
import org.springframework.security.core.Authentication

/**
 * Created by Muzammil on 1/14/21.
 */
interface IAuthenticationFacade {
  fun getUser(): User
}
