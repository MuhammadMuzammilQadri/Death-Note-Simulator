package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.exceptions.AppException
import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.repos.MemoryRepo
import com.muzammil.death_note_simulator.repos.UserRepo
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class OwnerService : IOwnerService {
  
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var personRepo: UserRepo
  
  @Autowired
  lateinit var memoryRepo: MemoryRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun makeOwner(deathNoteId: Long, personId: Long): User {
    deathNoteService.makeOwner(deathNoteId, personId)
    return personService.getPersonById(personId,
                                       shouldFetchFaces = true,
                                       shouldFetchDeathNotes = true)
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    memoryRepo.deleteAll()
  }
  
  @Transactional(readOnly = true)
  override fun listOwners(shouldFetchFaces: Boolean): List<User> {
    return when {
      shouldFetchFaces -> {
        personRepo.findOwnersWithFaces().toList()
      }
      else             -> {
        personRepo.findOwners().toList()
      }
    }
    
  }
  
  @Transactional(propagation = Propagation.REQUIRED)
  override fun killPerson(ownerId: Long, personToKillId: Long): User {
    return personService.getPersonById(personToKillId,
                                       shouldFetchFaces = false,
                                       shouldFetchDeathNotes = false).also { person ->
      if (!person.isAlive) {
        throw AppException(HttpStatus.IM_USED, "Person is already killed")
      }
      personRepo.updateIsAliveStatus(personToKillId, false).also {
        if (it > 0) {
          person.isAlive = false
        } else {
          throw UnknownException("Unable to kill person")
        }
      }
      addKillToOwnerMemory(ownerId, personToKillId)
    }
    
  }
  
  override fun getOwner(id: Long): User {
    return personRepo.findByIdAndDeathNotesNotNull(id)
           ?: throw DataNotFoundException("No owner exists with the specified id: $id")
  }
  
  // TODO: Change this - For now making every kill
  //  from the first notebook in the list of killer's notebooks
  private fun addKillToOwnerMemory(ownerId: Long, personToKillId: Long) {
    getOwner(ownerId).let { owner ->
      owner.deathNotes?.first()?.let { notebook ->
        memoryRepo.save(Memory(deathNote = notebook,
                               ownerPerson = owner,
                               killedPerson = User(id = personToKillId)))
      } ?: throw DataNotFoundException("${owner.name} has no DeathNotes")
    }
  }
  
  override fun getOwnerMemories(ownerId: Long): List<Memory> {
    return getOwner(ownerId).let { owner ->
      memoryRepo.findAllByOwnerPersonOrderById(owner)
    }
  }
}
