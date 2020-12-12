package com.muzammil.death_note_simulator.controllers.deathnote

import com.muzammil.death_note_simulator.models.ApiError
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Created by Muzammil on 11/22/20.
 */
@ControllerAdvice
class GlobalExceptionHandler {
  
  @ExceptionHandler(value = [DataAccessException::class, MethodArgumentNotValidException::class])
  fun handleDataAccessAndMethodArgumentNotValidException(ex: DataAccessException)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.NOT_ACCEPTABLE.value(),
                                   ex.localizedMessage ?: "Invalid data"),
                          HttpStatus.NOT_ACCEPTABLE)
  }
  
  @ExceptionHandler(value = [DataRetrievalFailureException::class])
  fun handleDataRetrievalFailureException(ex: DataRetrievalFailureException)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.NOT_FOUND.value(),
                                   ex.message ?: "Data not found"),
                          HttpStatus.NOT_FOUND)
  }
  
  @ExceptionHandler
  fun handleUnHandledException(ex: Exception)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST.value(),
                                   ex.message ?: "Something bad happened. Try again later"),
                          HttpStatus.BAD_REQUEST)
  }
}
