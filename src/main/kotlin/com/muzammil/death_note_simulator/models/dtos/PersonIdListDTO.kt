package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonIdListDTO(var facesList: List<PersonIdDTO>) {
  constructor() : this(mutableListOf())
}
