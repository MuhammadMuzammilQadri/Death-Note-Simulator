package com.muzammil.death_note_simulator.repos

import com.muzammil.death_note_simulator.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Muzammil on 12/1/20.
 */
@Component
class ReposManager {
  
  @Autowired
  private lateinit var deathNoteRepo: DeathNoteRepo
  
  @Autowired
  private lateinit var personRepo: UserRepo
  
  @Autowired
  private lateinit var memoryRepo: MemoryRepo
  
  @Autowired
  private lateinit var deathNoteHistoryRepo: DeathNoteHistoryRepo
  
  val logger = logger()
  
  fun deleteDataFromAllRepos() {
    logger.warn("Removing data from all repos...")
    memoryRepo.deleteAll()
    deathNoteHistoryRepo.deleteAll()
    deathNoteRepo.deleteAll()
    personRepo.deleteAll()
  }
}
