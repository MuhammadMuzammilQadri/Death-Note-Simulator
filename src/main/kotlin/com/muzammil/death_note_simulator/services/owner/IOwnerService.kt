package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.models.DeathNoteHistory
import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.Person

interface IOwnerService {
  fun makeOwner(deathNoteId: Long, personId: Long): Person
  fun getOwner(ownerName: String): Person?
  fun listOwners(): Set<Person>
  fun killPerson(ownerName: String, personToKill: String)
  fun deleteAll()
  fun getOwnerMemories(owner: Person): List<Memory>
  fun getOwnershipHistory(id: Long): List<DeathNoteHistory>
}
