package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonIdDTO(
  var id: Long? = null) : BaseDTO() {
  
  constructor(): this(null)
}
