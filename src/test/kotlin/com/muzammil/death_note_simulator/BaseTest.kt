package com.muzammil.death_note_simulator

import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Muzammil on 12/1/20.
 */
open class BaseTest {
  
  @Autowired
  private lateinit var deathNoteService: IDeathNoteService
  
  @Autowired
  private lateinit var personService: IPersonService
  
  @Autowired
  private lateinit var ownerService: IOwnerService
  
  open fun beforeEachSetup() {
    ownerService.deleteAll()
    deathNoteService.deleteAll()
    personService.deleteAll()
  }
}
