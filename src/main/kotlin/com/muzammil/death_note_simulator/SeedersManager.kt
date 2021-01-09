package com.muzammil.death_note_simulator

import com.muzammil.death_note_simulator.models.AppRole
import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.repos.ReposManager
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * Created by Muzammil on 12/1/20.
 */
@Component
class SeedersManager {
  
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var ownerService: IOwnerService
  
  val logger = logger()
  
  @Autowired
  lateinit var reposManager: ReposManager
  
  @PostConstruct
  fun seed() {
    reposManager.deleteDataFromAllRepos()
    createBadPerson()
    createOwner()
    createAdmin()
    logger.warn("Initialized seeders...")
  }
  
  private fun createBadPerson() = personService.savePerson(User(name = "Bad Guy"))
  
  private fun createAdmin() =
    personService.savePerson(User(name = "Muhammad Muzammil", roles = AppRole.ADMIN))
  
  
  private fun createOwner(): User {
    val deathNote = createDeathNote()
    val owner = personService.savePerson(User(name = "Light Yagami"))
    ownerService.makeOwner(deathNote.id!!, owner.id!!)
    return owner
  }
  
  private fun createDeathNote(): DeathNote {
    return deathNoteService.createOrUpdateNotebook(DeathNote(name = "Ryuk Notebook"))
  }
  
}
