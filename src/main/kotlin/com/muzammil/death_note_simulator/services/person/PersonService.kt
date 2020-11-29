package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.person.PersonRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonService : IPersonService {
  
  @Autowired
  lateinit var personRepo: PersonRepo
  
  override fun savePerson(person: Person): Person {
    return personRepo.save(person)
  }
  
  override fun getPerson(name: String): Person? {
    return personRepo.findByName(name)
  }
  
}
