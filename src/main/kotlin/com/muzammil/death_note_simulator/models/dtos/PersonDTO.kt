package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonDTO(
  var id: Long? = null,
  var name: String? = null,
  var isAlive: Boolean) : BaseDTO() {
  
  constructor() : this(null, null, true)
}
