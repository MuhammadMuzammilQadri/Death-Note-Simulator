package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class DeathNoteExceptOwnerDTO(
  var id: Long? = null,
  var name: String? = null) : BaseDTO() {
  
  constructor() : this(0, "")
}
