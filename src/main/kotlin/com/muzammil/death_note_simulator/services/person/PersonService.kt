package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.repos.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PersonService : IPersonService {
  
  private val ID_MUST_NOT_BE_NULL = "The given id must not be null!"
  
  @Autowired
  lateinit var personRepo: UserRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun savePerson(person: User): User {
    return personRepo.save(person)
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun saveAll(persons: Iterable<User>): Iterable<User> {
    return personRepo.saveAll(persons)
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    personRepo.deleteAll()
  }
  
  @Transactional(readOnly = true)
  override fun getPersonByName(name: String,
                               shouldFetchFaces: Boolean,
                               shouldFetchDeathNotes: Boolean): User {
    return personRepo.findByName(name)?.also {
      if (shouldFetchFaces) {
        it.facesSeen?.size
      }
      if (shouldFetchDeathNotes) {
        it.deathNotes?.size
      }
    } ?: throw DataNotFoundException("Person not found with the given name: $name")
  }
  
  @Transactional(readOnly = true)
  override fun listPersons(shouldFetchFaces: Boolean,
                           shouldFetchDeathNotes: Boolean): List<User> {
    return personRepo.findAll().also {
      if (shouldFetchFaces || shouldFetchDeathNotes) {
        it.forEach { person ->
          if (shouldFetchFaces) {
            person.facesSeen?.size
          }
          if (shouldFetchDeathNotes) {
            person.deathNotes?.size
          }
        }
      }
    }.toList()
  }
  
  @Transactional(readOnly = true)
  override fun getPersonById(personId: Long,
                             shouldFetchFaces: Boolean,
                             shouldFetchDeathNotes: Boolean): User {
    return personRepo.findById(personId).orElse(null)?.also {
      if (shouldFetchFaces) {
        it.facesSeen?.size
      }
      if (shouldFetchDeathNotes) {
        it.deathNotes?.size
      }
    } ?: throw DataNotFoundException("Person not found with the given id: $personId")
  }
  
  @Transactional
  override fun addFaceToPerson(id: Long?, faceIds: Array<Long?>): User {
    return personRepo.findById(id!!).orElse(null)?.let {
      it.facesSeen = (it.facesSeen?.toMutableSet() ?: mutableSetOf()).also {
        it.addAll(faceIds.map {
          User(id = it, name = "")
        })
      }
      savePerson(it)
    } ?: throw DataNotFoundException("Person not found with the given id: $id")
  }
}
