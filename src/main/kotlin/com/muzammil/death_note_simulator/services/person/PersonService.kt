package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.person.PersonRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PersonService : IPersonService {
  
  @Autowired
  lateinit var personRepo: PersonRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun savePerson(person: Person): Person {
    return personRepo.save(person)
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun saveAll(persons: Iterable<Person>): Iterable<Person> {
    return personRepo.saveAll(persons)
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    personRepo.deleteAll()
  }
  
  @Transactional(readOnly = true, noRollbackFor = [Exception::class])
  override fun getPerson(name: String,
                         shouldFetchFaces: Boolean,
                         shouldFetchDeathNotes: Boolean): Person? {
    return personRepo.findByName(name)?.also {
      if (shouldFetchFaces) {
        it.facesSeen.size
      }
      if (shouldFetchDeathNotes) {
        it.deathNotes.size
      }
    }
  }
  
  @Transactional(readOnly = true)
  override fun getPersonById(personId: Long): Person? {
    return personRepo.findById(personId).get()
  }
  
  @Transactional(readOnly = true)
  override fun findAllByDeathNotesNotNull(): Set<Person> {
    return personRepo.findAllByDeathNotesNotNull()
  }
}
