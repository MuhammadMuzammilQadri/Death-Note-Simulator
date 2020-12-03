package com.muzammil.death_note_simulator.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

/**
 * Created by Muzammil on 12/3/20.
 */
class UnknownException(message: String)
  : HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, message) {
}
