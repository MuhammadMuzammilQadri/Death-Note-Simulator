package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.BaseTest
import com.muzammil.death_note_simulator.models.Person
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
class PersonServiceTest : BaseTest() {
  
  @Autowired
  lateinit var personService: IPersonService
  
  @BeforeEach
  override fun beforeEachSetup() {
    super.beforeEachSetup()
  }
  
  @Test
  fun createAUser_addFaces_thenFindByName() {
    val personLight = Person(name = "Light")
    val personYagami = Person(name = "Yagami")
    val personL = Person(name = "L")
    
    personService.saveAll(listOf(personLight, personYagami, personL))
    personYagami.facesSeen = mutableSetOf(personLight, personL)
    personService.savePerson(personYagami)
    
    val fetchedPerson = personService.getPerson("Yagami", shouldFetchFaces = true)
    
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
