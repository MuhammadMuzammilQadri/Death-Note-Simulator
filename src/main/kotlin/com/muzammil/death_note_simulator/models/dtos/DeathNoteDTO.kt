package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class DeathNoteDTO(
  var id: Long? = null,
  var name: String? = null,
  var owner: PersonDTO?) : BaseDTO() {
  
  constructor() : this(0, "", null)
}
