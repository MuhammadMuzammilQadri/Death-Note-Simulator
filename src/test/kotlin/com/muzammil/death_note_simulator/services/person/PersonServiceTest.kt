package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.ReposManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException

/**
 * Created by Muzammil on 11/29/20.
 */

@SpringBootTest
class PersonServiceTest {
  
  @Autowired
  lateinit var personService: IPersonService
  
  
  @Autowired
  lateinit var reposManager: ReposManager
  
  @BeforeEach
  fun beforeEachSetup() {
    reposManager.deleteDataFromAllRepos()
  }
  
  @Test
  fun createAUser_addFaces_thenFindByName() {
    val personLight = Person(name = "Light")
    var personYagami = Person(name = "Yagami")
    val personL = Person(name = "L")
    
    personService.saveAll(listOf(personLight, personYagami, personL))
    personYagami.facesSeen = mutableSetOf(personLight)
    
    // then refetch person from db
    personService.savePerson(personYagami).also {
      personYagami = personService.getPersonById(it.id!!, shouldFetchFaces = true)
    }
    
    // then add more faces
    personYagami.facesSeen = personYagami.facesSeen.toMutableSet().also {
      it.addAll(mutableSetOf(personL))
    }
    personService.savePerson(personYagami)
    
    // assert
    val fetchedPerson = personService.getPersonByName("Yagami", shouldFetchFaces = true)
    
    assertNotNull(fetchedPerson)
    assertEquals(personYagami.id, fetchedPerson?.id)
    assertEquals(personYagami.facesSeen.size, fetchedPerson?.facesSeen?.size)
    assertNotNull(fetchedPerson?.facesSeen?.find { it.name == personLight.name })
    assertNotNull(fetchedPerson?.facesSeen?.find { it.name == personL.name })
  }
  
  @Test
  fun addANotExistentFaceToTheListOfPerson_thenFails() {
    val personYagami = Person(name = "Yagami")
    personService.savePerson(personYagami)
    
    val personGoku = Person(name = "Goku")
    personYagami.facesSeen = mutableSetOf(personGoku)
    
    assertThrows(InvalidDataAccessApiUsageException::class.java) {
      personService.savePerson(personYagami)
    }
  }
  
}
