package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class PersonsListWithoutFacesDTO(
  var data: List<PersonDTO>) : PersonsListDTO() {
  constructor() : this(mutableListOf())
}
