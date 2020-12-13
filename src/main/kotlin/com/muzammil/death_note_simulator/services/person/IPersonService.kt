package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person

/**
 * Created by Muzammil on 11/29/20.
 */
interface IPersonService {
  fun savePerson(person: Person): Person
  fun saveAll(persons: Iterable<Person>): Iterable<Person>
  fun getPersonByName(name: String,
                      shouldFetchFaces: Boolean = false,
                      shouldFetchDeathNotes: Boolean = false): Person
  
  fun deleteAll()
  fun getPersonById(personId: Long,
                    shouldFetchFaces: Boolean = false,
                    shouldFetchDeathNotes: Boolean = false): Person
  
  fun listPersons(shouldFetchFaces: Boolean = false,
                  shouldFetchDeathNotes: Boolean = false): List<Person>
  
  fun addFaceToPerson(id: Long?, faceIds: Array<Long?>): Person
}
