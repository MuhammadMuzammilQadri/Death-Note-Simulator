package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/14/20.
 */
class CreateOwnerRequestDTO(
  var ownerId: Long,
  var deathNoteId: Long) : BaseDTO() {
}
