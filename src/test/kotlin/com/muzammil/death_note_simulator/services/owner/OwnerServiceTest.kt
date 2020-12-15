package com.muzammil.death_note_simulator.services.owner

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.repos.ReposManager
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.hibernate.LazyInitializationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by Muzammil on 11/30/20.
 */
@SpringBootTest
class OwnerServiceTest {
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
  fun createAPerson_thenMakeHimOwner_assertOwnersDeathNotesAreFetchingLazy() {
    // given
    createDeathNote().let { deathNote ->
      personService.savePerson(Person(name = "Light Yagami")).let {
        // then
        ownerService.makeOwner(deathNote.id!!, it.id!!)
      }
      // assert
      val fetchedDeathNote = deathNoteService.findNotebook(deathNoteId = deathNote.id!!)
      assertThrows<LazyInitializationException> {
        fetchedDeathNote.owner?.deathNotes?.size
      }
    }
  }
  
  @Test
  fun createAPerson_thenMakeHimOwner_assertOwnership() {
    // given
    val deathNote = createDeathNote()
    var owner = personService.savePerson(Person(name = "Light Yagami"))
    
    // then
    ownerService.makeOwner(deathNote.id!!, owner.id!!)
    
    // assert
    val ownersList = ownerService.listOwners(shouldFetchFaces = true)
    owner = personService.getPersonByName(ownersList.first().name ?: "",
                                          shouldFetchDeathNotes = true)!!
    assertEquals(1, ownersList.size)
    assertEquals(1, owner.deathNotes?.size)
  }
  
  @Test
  fun givenPersonAndOwner_thenKillPerson_assertPersonIsNotAlive() {
    // given
    val person = personService.savePerson(Person(name = "Bad Guy"))
    val owner = createOwner()
    
    // then
    ownerService.killPerson(owner.id!!, person.id!!)
    
    // assert
    assertEquals(1, ownerService.listOwners().size)
    assertEquals(false, personService.getPersonByName(person.name ?: "").isAlive)
  }
  
  @Test
  fun givenAPersonsList_thenKillThem_assertOwnerHasMemories() {
    // given
    val person1 = personService.savePerson(Person(name = "Bad Guy1"))
    val person2 = personService.savePerson(Person(name = "Bad Guy2"))
    val person3 = personService.savePerson(Person(name = "Bad Guy3"))
    val person4 = personService.savePerson(Person(name = "Bad Guy4"))
    val owner = createOwner()
    
    // then
    ownerService.killPerson(owner.id!!, person1.id!!)
    ownerService.killPerson(owner.id!!, person2.id!!)
    ownerService.killPerson(owner.id!!, person3.id!!)
    ownerService.killPerson(owner.id!!, person4.id!!)
    
    // assert
    val ownerMemories = ownerService.getOwnerMemories(owner.id!!)
    val killedPersons = ownerMemories.map { memory ->
      memory.killedPerson
    }
    assertEquals(4, ownerMemories.size)
    assertEquals(person1.id, killedPersons[0]?.id)
    assertEquals(person2.id, killedPersons[1]?.id)
    assertEquals(person3.id, killedPersons[2]?.id)
    assertEquals(person4.id, killedPersons[3]?.id)
  }
  
  @Test
  fun givenAPersonsList_thenKillThemAndRemoveOwner_assertOwnerHasNoMemories() {
    // given
    val person1 = personService.savePerson(Person(name = "Bad Guy1"))
    val person2 = personService.savePerson(Person(name = "Bad Guy2"))
    val person3 = personService.savePerson(Person(name = "Bad Guy3"))
    val person4 = personService.savePerson(Person(name = "Bad Guy4"))
    val owner = createOwner()
    
    // then
    ownerService.killPerson(owner.id!!, person1.id!!)
    ownerService.killPerson(owner.id!!, person2.id!!)
    ownerService.killPerson(owner.id!!, person3.id!!)
    ownerService.killPerson(owner.id!!, person4.id!!)
    val newOwner = personService.savePerson(Person(name = "Misa"))
    ownerService.makeOwner(owner.deathNotes?.first()?.id!!, newOwner.id!!)
    
    // assert
    assertThrows<DataNotFoundException> {
      val ownerMemories = ownerService.getOwnerMemories(owner.id!!)
    }
  }
  
  private fun createOwner(): Person {
    val deathNote = createDeathNote()
    val owner = personService.savePerson(Person(name = "Light Yagami"))
    return ownerService.makeOwner(deathNote.id!!, owner.id!!)
  }
  
  private fun createDeathNote(): DeathNote {
    return deathNoteService
      .createOrUpdateNotebook(DeathNote(name = "Ryuk Notebook"))
  }
}
