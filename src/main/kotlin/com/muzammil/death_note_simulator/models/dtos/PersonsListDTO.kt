package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonsListDTO(var data: List<PersonDTO>) {
  constructor() : this(mutableListOf())
}
