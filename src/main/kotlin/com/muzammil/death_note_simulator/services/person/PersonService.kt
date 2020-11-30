package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.person.PersonRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonService : IPersonService {
  
  @Autowired
  lateinit var personRepo: PersonRepo
  
  override fun savePerson(person: Person): Person {
    return personRepo.save(person)
  }
  
  override fun saveAll(persons: Iterable<Person>): Iterable<Person> {
    return personRepo.saveAll(persons)
  }
  
  override fun deleteAll() {
    personRepo.deleteAll()
  }
  
  @Transactional(readOnly = true, noRollbackFor = [Exception::class])
  override fun getPerson(name: String, shouldFetchFaces: Boolean): Person? {
    return if (shouldFetchFaces) {
      getPersonWithSeenFaces(name)
    } else {
      getPerson(name)
    }
  }
  
  private fun getPerson(name: String): Person? {
    return personRepo.findByName(name)
  }
  
  protected fun getPersonWithSeenFaces(name: String): Person? {
    return personRepo.findByName(name)?.also {
      it.facesSeen.size
    }
  }
  
}
