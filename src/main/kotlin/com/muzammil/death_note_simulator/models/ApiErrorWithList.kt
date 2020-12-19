package com.muzammil.death_note_simulator.models

import org.springframework.http.HttpStatus

/**
 * Created by Muzammil on 11/28/20.
 */
class ApiErrorWithList(val status: Int? = HttpStatus.BAD_REQUEST.value(),
                       val message: String? = "Please resolve the following errors",
                       val errors: List<String>)
