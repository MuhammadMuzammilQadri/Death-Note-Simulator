package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.Person

interface IOwnerService {
  fun makeOwner(deathNoteId: Long, personId: Long): Person
  fun getOwner(id: Long): Person?
  fun listOwners(shouldFetchFaces: Boolean = false,
                 shouldFetchDeathNotes: Boolean = false): List<Person>
  
  fun killPerson(ownerId: Long, personToKillId: Long)
  fun deleteAll()
  fun getOwnerMemories(ownerId: Long): List<Memory>
}
