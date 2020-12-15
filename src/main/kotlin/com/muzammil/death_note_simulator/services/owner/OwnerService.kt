package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.memory.MemoryRepo
import com.muzammil.death_note_simulator.repos.person.PersonRepo
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.springframework.beans.factory.annotation.Autowired
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
  lateinit var personRepo: PersonRepo
  
  @Autowired
  lateinit var memoryRepo: MemoryRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun makeOwner(deathNoteId: Long, personId: Long): Person {
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
  override fun listOwners(shouldFetchFaces: Boolean): List<Person> {
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
  override fun killPerson(ownerId: Long, personToKillId: Long) {
    personRepo.updateIsAliveStatus(personToKillId, false)
    addKillToOwnerMemory(ownerId, personToKillId)
  }
  
  override fun getOwner(id: Long): Person {
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
                               killedPerson = Person(id = personToKillId)))
      } ?: throw DataNotFoundException("${owner.name} has no DeathNotes")
    }
  }
  
  override fun getOwnerMemories(ownerId: Long): List<Memory> {
    return getOwner(ownerId).let { owner ->
      memoryRepo.findAllByOwnerPersonOrderById(owner)
    }
  }
}
