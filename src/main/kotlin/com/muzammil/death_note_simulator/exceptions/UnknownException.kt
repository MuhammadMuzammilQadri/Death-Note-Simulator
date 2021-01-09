package com.muzammil.death_note_simulator.exceptions

import org.springframework.http.HttpStatus

/**
 * Created by Muzammil on 12/3/20.
 */
class UnknownException(message: String)
  : AppException(HttpStatus.INTERNAL_SERVER_ERROR, message) {
}
