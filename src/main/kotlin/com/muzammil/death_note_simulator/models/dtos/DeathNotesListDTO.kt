package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class DeathNotesListDTO(var data: List<DeathNoteExceptOwnerDTO>) {
  constructor() : this(mutableListOf())
}
