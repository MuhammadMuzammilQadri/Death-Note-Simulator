package com.muzammil.death_note_simulator

import com.muzammil.death_note_simulator.models.AppAuthority
import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.repos.ReposManager
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import com.muzammil.death_note_simulator.services.person.IPersonService
import com.muzammil.death_note_simulator.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Muzammil on 12/1/20.
 */
@Component
class SeedersManager : ApplicationListener<ContextRefreshedEvent> {
  
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var ownerService: IOwnerService
  
  val logger = logger()
  
  @Autowired
  lateinit var reposManager: ReposManager
  
  @Transactional
  override fun onApplicationEvent(event: ContextRefreshedEvent) {
    logger.warn("Initializing Seeders...")
    reposManager.deleteDataFromAllRepos()
    createBadPerson()
    createOwner()
    createAdmin()
    logger.warn("Seeders Initialized Successfully...")
  }
  
  private fun createBadPerson() = personService.savePerson(User(name = "Bad Guy"))
  
  private fun createAdmin() =
    personService.savePerson(User(name = "Muhammad Muzammil", roles = AppAuthority.ADMIN))
  
  
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
