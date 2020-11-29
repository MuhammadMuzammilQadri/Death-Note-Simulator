package com.muzammil.death_note_simulator.services.person

import com.muzammil.death_note_simulator.models.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by Muzammil on 11/29/20.
 */

@SpringBootTest
class PersonServiceTest {
  @Autowired
  lateinit var personService: IPersonService
  
  @Test
  fun createAUser_addFaces_thenFindByName() {
    val personLight = Person(name = "Light")
    val personYagami = Person(name = "Yagami")
    val personL = Person(name = "L")
    personYagami.facesSeen = mutableListOf(personLight, personL)
    
    personService.savePerson(personYagami)
    
    val fetchedPerson = personService.getPerson("Yagami")
    assertNotNull(fetchedPerson)
    assertEquals(personYagami.id, fetchedPerson?.id)
    assertEquals(personYagami.facesSeen.size, fetchedPerson?.facesSeen?.size)
    assertNotNull(fetchedPerson?.facesSeen?.find { it.name == personLight.name })
    assertNotNull(fetchedPerson?.facesSeen?.find { it.name == personL.name })
  }
  
}
