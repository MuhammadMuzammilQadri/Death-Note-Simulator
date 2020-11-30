package com.muzammil.death_note_simulator.repos.person

import com.muzammil.death_note_simulator.models.Person
import org.springframework.data.repository.CrudRepository

/**
 * Created by Muzammil on 11/29/20.
 */
interface PersonRepo : CrudRepository<Person, Long> {
  fun findByName(name: String): Person?
  fun findAllByDeathNotesNotNull(): Set<Person>
  fun deleteByName(name: String)
}
