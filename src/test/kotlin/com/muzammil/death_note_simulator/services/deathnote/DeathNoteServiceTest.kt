package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.BaseTest
import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by Muzammil on 11/29/20.
 */
@SpringBootTest
class DeathNoteServiceTest {
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var ownerService: IOwnerService
  
  @BeforeEach
  fun beforeEachSetup() {
    deathNoteService.deleteAll()
    personService.deleteAll()
    ownerService.deleteAll()
  }
  
  @Test
  fun saveDeathNote_thenGetList() {
    val deathNote1 = DeathNote(name = "Light Book")
    val deathNote2 = DeathNote(name = "Muzammil Book")
    deathNoteService.createOrUpdateNotebook(deathNote1)
    deathNoteService.createOrUpdateNotebook(deathNote2)
    
    val fetchedDeathNoteList = deathNoteService.listNotebooks()
    assertEquals(2, fetchedDeathNoteList.size)
    assertNotNull(fetchedDeathNoteList.find { it.name == deathNote1.name })
    assertNotNull(fetchedDeathNoteList.find { it.name == deathNote2.name })
  }
}
