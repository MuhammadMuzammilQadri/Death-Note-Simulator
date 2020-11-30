package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by Muzammil on 11/30/20.
 */
@SpringBootTest
class OwnerServiceTest {
  
  @Autowired
  lateinit var ownerService: IOwnerService
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @Test
  fun givenAPerson_createAOwnerToKillHim() {
    // given
    val person = personService.savePerson(Person(name = "Bad Guy"))
    val owner = createOwner()
    
    // then
    ownerService.killPerson(owner.name, person.name)
    
    // assert
    assertEquals(1, ownerService.listOwners().size)
    assertNull(personService.getPerson(person.name))
  }
  
  private fun createOwner(): Person {
    val deathNote = createDeathNote()
    val owner = personService.savePerson(Person(name = "Light Yagami"))
    ownerService.makeOwner(deathNote.id!!, owner.id!!)
    return owner
  }
  
  private fun createDeathNote(): DeathNote {
    return deathNoteService.createOrUpdateNotebook(DeathNote(name = "Ryuk Notebook"))
  }
  
}
