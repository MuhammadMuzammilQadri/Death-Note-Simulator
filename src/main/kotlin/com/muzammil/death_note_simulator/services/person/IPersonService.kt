package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person

/**
 * Created by Muzammil on 11/29/20.
 */
interface IPersonService {
  fun savePerson(person: Person): Person
  fun getPerson(name: String): Person?
  
}
