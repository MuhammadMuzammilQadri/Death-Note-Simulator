package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonWithFacesDTO(
  var id: Long? = null,
  var name: String? = null,
  var facesSeen: Set<PersonDTO> = mutableSetOf()) : BaseDTO() {
  constructor() : this(null, null)
}
