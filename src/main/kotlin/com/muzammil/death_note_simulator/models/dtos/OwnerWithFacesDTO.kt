package com.muzammil.death_note_simulator.models.dtos

import com.muzammil.death_note_simulator.models.DeathNote

/**
 * Created by Muzammil on 12/9/20.
 */
class OwnerWithFacesDTO(
  var id: Long? = null,
  var name: String? = null,
  var facesSeen: Set<PersonDTO>? = mutableSetOf(),
  var deathNotes: Set<DeathNoteDTO>? = mutableSetOf()) : BaseDTO() {
  constructor() : this(null, null)
}
