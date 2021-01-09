package com.muzammil.death_note_simulator.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException

/**
 * Created by Muzammil on 12/3/20.
 */
open class AppException(statusCode: HttpStatus, statusText: String) :
  HttpStatusCodeException(statusCode, statusText) {
}
