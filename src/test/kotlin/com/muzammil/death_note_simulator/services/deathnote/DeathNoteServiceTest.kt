package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.ReposManager
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
  
  @Autowired
  lateinit var reposManager: ReposManager
  
  @BeforeEach
  fun beforeEachSetup() {
    reposManager.deleteDataFromAllRepos()
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
  
  @Test
  fun givenDeathNote_thenChangeOwners_assertHistorySaves() {
    // given
    val deathNote = createDeathNote()
    val firstOwner = personService.savePerson(Person(name = "Light Yagami"))
    ownerService.makeOwner(deathNote.id!!, firstOwner.id!!)
    val secondOwner = personService.savePerson(Person(name = "Misa"))
    ownerService.makeOwner(deathNote.id!!, secondOwner.id!!)
    ownerService.makeOwner(deathNote.id!!, firstOwner.id!!)
    
    // then
    val ownershipHistory = deathNoteService.getOwnershipHistoryOf(deathNote.id!!)
    
    // assert
    assertEquals(3, ownershipHistory.size)
    assertEquals(firstOwner.id, ownershipHistory[0].owner?.id)
    assertEquals(secondOwner.id, ownershipHistory[1].owner?.id)
    assertEquals(firstOwner.id, ownershipHistory[2].owner?.id)
  }
  
  private fun createDeathNote(): DeathNote {
    return deathNoteService
      .createOrUpdateNotebook(DeathNote(name = "Ryuk Notebook"))
  }
  
}
