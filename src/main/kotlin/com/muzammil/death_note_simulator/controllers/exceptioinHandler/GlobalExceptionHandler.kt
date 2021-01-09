package com.muzammil.death_note_simulator.controllers.exceptioinHandler

import com.muzammil.death_note_simulator.exceptions.AppException
import com.muzammil.death_note_simulator.models.ApiError
import com.muzammil.death_note_simulator.models.ApiErrorWithList
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

/**
 * Created by Muzammil on 11/22/20.
 */
@ControllerAdvice
class GlobalExceptionHandler {
  
  @ExceptionHandler(value = [
    DataAccessException::class,
    DataRetrievalFailureException::class])
  fun dataAccessAndMethodArgumentNotValidFailures(ex: Exception)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.NOT_ACCEPTABLE.value(),
                                   ex.cause?.message ?: "Invalid data"),
                          HttpStatus.NOT_ACCEPTABLE)
  }
  
  @ExceptionHandler(value = [MethodArgumentNotValidException::class])
  fun methodArgumentNotValidException(ex: MethodArgumentNotValidException) =
    ex.bindingResult
      .allErrors
      .map {
        it.defaultMessage ?: "Invalid data"
      }
      .toList()
      .let {
        ResponseEntity(ApiErrorWithList(errors = it),
                       HttpStatus.NOT_ACCEPTABLE)
      }
  
  
  @ExceptionHandler(value = [ConstraintViolationException::class])
  fun constraintViolationFailures(ex: ConstraintViolationException) =
    ex.constraintViolations
      .map {
        it.message
      }
      .toList()
      .let {
        ResponseEntity(ApiErrorWithList(errors = it),
                       HttpStatus.NOT_ACCEPTABLE)
      }
  
  
  @ExceptionHandler(value = [AppException::class])
  fun appExceptions(ex: AppException)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(ex.statusCode.value(),
                                   ex.cause?.message ?: "Something bad happened. Try again later"),
                          ex.statusCode)
  }
  
  @ExceptionHandler
  fun unHandledFailures(ex: Exception)
    : ResponseEntity<ApiError> {
    return ResponseEntity(ApiError(HttpStatus.BAD_REQUEST.value(),
                                   ex.cause?.message ?: "Something bad happened. Try again later"),
                          HttpStatus.BAD_REQUEST)
  }
}
