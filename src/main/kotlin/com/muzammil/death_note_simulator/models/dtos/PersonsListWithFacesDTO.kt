package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonsListWithFacesDTO(
  var data: List<PersonWithFacesDTO>) : PersonsListDTO() {
  constructor() : this(mutableListOf())
}
