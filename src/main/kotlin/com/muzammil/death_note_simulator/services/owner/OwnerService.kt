package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.DeathNoteHistory
import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.deathnote_history.DeathNoteHistoryRepo
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
  
  @Autowired
  lateinit var deathNoteHistoryRepo: DeathNoteHistoryRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun makeOwner(deathNoteId: Long, personId: Long): Person {
    deathNoteService.findNotebook(deathNoteId).also { deathNote ->
      personService.getPersonById(personId).also { person ->
        deathNote.owner = person
        deathNoteHistoryRepo.save(DeathNoteHistory(deathNote = deathNote,
                                                   owner = person))
        deathNoteService.createOrUpdateNotebook(deathNote)
        return personService.getPersonById(personId,
                                           shouldFetchFaces = true,
                                           shouldFetchDeathNotes = true)
      }
    }
  }
  
  override fun getOwnershipHistory(id: Long): List<DeathNoteHistory> {
    return deathNoteHistoryRepo.findAllByDeathNoteOrderById(
      deathNoteService.findNotebook(id))
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
      // TODO: Change to SQL command
      personService.getPerson(personToKill).let { killedPerson ->
        killedPerson.isAlive = false
        personRepo.save(killedPerson)
        addKillToOwnerMemory(owner, killedPerson)
      }
    } ?: throw DataNotFoundException("No owner exists with the specified name: $ownerName")
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
    getOwner(owner.name)?.let {
      return memoryRepo.findAllByOwnerPersonOrderById(owner)
    } ?: return mutableListOf()
  }
}
