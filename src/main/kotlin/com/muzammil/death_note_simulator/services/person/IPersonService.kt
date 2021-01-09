package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.User

/**
 * Created by Muzammil on 11/29/20.
 */
interface IPersonService {
  fun savePerson(person: User): User
  fun saveAll(persons: Iterable<User>): Iterable<User>
  fun getPersonByName(name: String,
                      shouldFetchFaces: Boolean = false,
                      shouldFetchDeathNotes: Boolean = false): User
  
  fun deleteAll()
  fun getPersonById(personId: Long,
                    shouldFetchFaces: Boolean = false,
                    shouldFetchDeathNotes: Boolean = false): User
  
  fun listPersons(shouldFetchFaces: Boolean = false,
                  shouldFetchDeathNotes: Boolean = false): List<User>
  
  fun addFaceToPerson(id: Long?, faceIds: Array<Long?>): User
}
