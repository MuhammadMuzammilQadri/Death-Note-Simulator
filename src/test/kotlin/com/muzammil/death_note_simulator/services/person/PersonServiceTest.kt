package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.repos.ReposManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException

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
    
    // given
    val personLight = User(name = "Light")
    val personYagami = User(name = "Yagami")
    val personL = User(name = "L")
    personService.saveAll(listOf(personLight, personYagami, personL))
    
    // then
    personService.addFaceToPerson(personYagami.id, arrayOf(personLight.id))
    personService.addFaceToPerson(personYagami.id, arrayOf(personL.id))
    
    // assert
    val fetchedPerson = personService.getPersonByName("Yagami", shouldFetchFaces = true)
    
    assertNotNull(fetchedPerson)
    assertEquals(personYagami.id, fetchedPerson.id)
    assertEquals(2, fetchedPerson.facesSeen?.size)
    assertNotNull(fetchedPerson.facesSeen?.find { it.name == personLight.name })
    assertNotNull(fetchedPerson.facesSeen?.find { it.name == personL.name })
  }
  
  @Test
  fun addANotExistentFaceToTheListOfPerson_thenFails() {
    // given
    val personYagami = User(name = "Yagami")
    personService.savePerson(personYagami)
    
    // then add a non-existent person's face
    // and assert exception occurs
    assertThrows<JpaObjectRetrievalFailureException> {
      personService.addFaceToPerson(personYagami.id, arrayOf(2335))
    }
  }
  
}
