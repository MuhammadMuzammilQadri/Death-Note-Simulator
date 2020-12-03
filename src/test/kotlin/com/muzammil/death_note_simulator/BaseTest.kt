package com.muzammil.death_note_simulator

import com.muzammil.death_note_simulator.repos.deathnote.DeathNoteRepo
import com.muzammil.death_note_simulator.repos.deathnote_history.DeathNoteHistoryRepo
import com.muzammil.death_note_simulator.repos.memory.MemoryRepo
import com.muzammil.death_note_simulator.repos.person.PersonRepo
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Muzammil on 12/1/20.
 */
open class BaseTest {
  
  @Autowired
  private lateinit var deathNoteRepo: DeathNoteRepo
  
  @Autowired
  private lateinit var personRepo: PersonRepo
  
  @Autowired
  private lateinit var memoryRepo: MemoryRepo
  
  @Autowired
  private lateinit var deathNoteHistoryRepo: DeathNoteHistoryRepo
  
  open fun beforeEachSetup() {
    memoryRepo.deleteAll()
    deathNoteHistoryRepo.deleteAll()
    deathNoteRepo.deleteAll()
    personRepo.deleteAll()
  }
}
