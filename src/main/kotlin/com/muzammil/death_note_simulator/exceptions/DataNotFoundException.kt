package com.muzammil.death_note_simulator.exceptions

import org.springframework.http.HttpStatus

/**
 * Created by Muzammil on 12/3/20.
 */
class DataNotFoundException(message: String)
  : AppException(HttpStatus.NOT_FOUND, message) {
}
