package com.muzammil.death_note_simulator.models.dtos

/**
 * Created by Muzammil on 12/9/20.
 */
class OwnersListWithFacesDTO(
  var data: List<OwnerWithFacesDTO>) : PersonsListDTO() {
  constructor() : this(mutableListOf())
}
