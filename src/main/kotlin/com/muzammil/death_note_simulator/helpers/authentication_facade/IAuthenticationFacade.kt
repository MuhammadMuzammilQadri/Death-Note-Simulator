package com.muzammil.death_note_simulator.helpers.authentication_facade

import com.muzammil.death_note_simulator.models.User

/**
 * Created by Muzammil on 1/14/21.
 */
interface IAuthenticationFacade {
  fun getUser(): User
}
