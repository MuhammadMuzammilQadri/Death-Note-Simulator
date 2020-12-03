package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.DeathNote
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
import java.util.*

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
  override fun makeOwner(deathNoteId: Long, personId: Long): DeathNote {
    deathNoteService.findNotebook(deathNoteId)?.also { deathNote ->
      personService.getPersonById(personId)?.also { person ->
        person.deathNotes = mutableSetOf(deathNote)
        deathNote.owner = person
        personService.savePerson(person)
        return deathNoteService.createOrUpdateNotebook(deathNote)
      }
    }
    throw NoSuchElementException("Do Death Note present with the given id")
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    memoryRepo.deleteAll()
  }
  
  @Transactional(readOnly = true)
  override fun listOwners(): Set<Person> {
    return personService.findAllByDeathNotesNotNull()
  }
  
  @Transactional(propagation = Propagation.REQUIRED)
  override fun killPerson(ownerName: String, personToKill: String) {
    getOwner(ownerName)?.let { owner ->
      personService.getPerson(personToKill)?.let { killedPerson ->
        killedPerson.isAlive = false
        personRepo.save(killedPerson)
        addKillToOwnerMemory(owner, killedPerson)
      } ?: throw DataNotFoundException("Unable to kill person. No person exists with the specified name")
    } ?: throw DataNotFoundException("Unable to kill person. No person exists with the specified name")
  }
  
  override fun getOwner(ownerName: String): Person? {
    return personRepo.findByNameAndDeathNotesNotNull(ownerName)
  }
  
  private fun addKillToOwnerMemory(owner: Person, killedPerson: Person) {
    memoryRepo.save(Memory(deathNote = owner.deathNotes.first(),
                           ownerPerson = owner,
                           killedPerson = killedPerson))
  }
  
  override fun getOwnerMemories(owner: Person): List<Memory> {
    return memoryRepo.findAllByOwnerPersonOrderById(owner)
  }
}
