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
  override fun listOwners(shouldFetchFaces: Boolean,
                          shouldFetchDeathNotes: Boolean): List<Person> {
    return if (shouldFetchFaces || shouldFetchDeathNotes) {
      personRepo.findAllByDeathNotesNotNull().also {
        it.forEach { person ->
          if (shouldFetchFaces) {
            person.facesSeen?.size
          }
          if (shouldFetchDeathNotes) {
            person.deathNotes?.size
          }
        }
      }.also {
        it.forEach { person ->
          if (!shouldFetchFaces) {
            person.facesSeen = null
          }
          if (!shouldFetchDeathNotes) {
            person.deathNotes = null
          }
        }
      }.toList()
    } else {
      personRepo.findAllByDeathNotesNotNull().toList()
    }
    
  }
  
  @Transactional(propagation = Propagation.REQUIRED)
  override fun killPerson(ownerName: String, personToKill: String) {
    getOwner(ownerName)?.let { owner ->
      personRepo.updateIsAliveStatus(personToKill, false)
      addKillToOwnerMemory(owner, personService.getPersonByName(name = personToKill))
    } ?: throw DataNotFoundException("No owner exists with the specified name: $ownerName")
  }
  
  override fun getOwner(ownerName: String): Person? {
    return personRepo.findByNameAndDeathNotesNotNull(ownerName)
  }
  
  private fun addKillToOwnerMemory(owner: Person, killedPerson: Person) {
    memoryRepo.save(Memory(deathNote = owner.deathNotes?.first(),
                           ownerPerson = owner,
                           killedPerson = killedPerson))
  }
  
  override fun getOwnerMemories(owner: Person): List<Memory> {
    owner.name?.let {
      getOwner(it)?.let {
        return memoryRepo.findAllByOwnerPersonOrderById(owner)
      }
    }
    ?: throw DataNotFoundException("No owner exists with the specified name: ${owner.name}")
  }
}
