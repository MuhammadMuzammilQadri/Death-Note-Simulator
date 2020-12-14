package com.muzammil.death_note_simulator.controllers.exceptioinHandler

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.ApiError
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.RestClientResponseException

/**
 * Created by Muzammil on 11/22/20.
 */
@ControllerAdvice
class GlobalExceptionHandler {
  
  @ExceptionHandler(value = [
    DataAccessException::class,
    DataRetrievalFailureException::class,
    MethodArgumentNotValidException::class])
  fun dataAccessAndMethodArgumentNotValidFailures(ex: Exception)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.NOT_ACCEPTABLE.value(),
                                   ex.cause?.message ?: "Invalid data"),
                          HttpStatus.NOT_ACCEPTABLE)
  }
  
  @ExceptionHandler(value = [
    DataNotFoundException::class])
  fun dataRetrievalAndNotFoundFailures(ex: RestClientResponseException)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.NOT_FOUND.value(),
                                   ex.statusText),
                          HttpStatus.NOT_FOUND)
  }
  
  @ExceptionHandler
  fun unHandledFailures(ex: Exception)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST.value(),
                                   ex.cause?.message ?: "Something bad happened. Try again later"),
                          HttpStatus.BAD_REQUEST)
  }
}
