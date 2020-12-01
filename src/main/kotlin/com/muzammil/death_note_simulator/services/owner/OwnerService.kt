package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.Person
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
    personService.deleteAll()
  }
  
  @Transactional(readOnly = true)
  override fun listOwners(): Set<Person> {
    return personService.findAllByDeathNotesNotNull()
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun killPerson(ownerName: String, personToKill: String) {
    personService.getPerson(ownerName, true).also {
      if (it?.deathNotes?.isEmpty() == false) {
        personService.deletePerson(personToKill)
      }
    }
  }
}
