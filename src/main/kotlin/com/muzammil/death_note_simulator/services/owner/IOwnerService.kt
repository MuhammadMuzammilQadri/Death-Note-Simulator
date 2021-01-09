package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.User

interface IOwnerService {
  fun makeOwner(deathNoteId: Long, personId: Long): User
  fun getOwner(id: Long): User?
  fun listOwners(shouldFetchFaces: Boolean = false): List<User>
  
  fun killPerson(ownerId: Long, personToKillId: Long)
  fun deleteAll()
  fun getOwnerMemories(ownerId: Long): List<Memory>
}
