package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.repos.deathnote.DeathNoteRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeathNoteService : IDeathNoteService {
  
  @Autowired
  lateinit var deathNoteRepo: DeathNoteRepo
  
  override fun saveNotebook(deathNote: DeathNote): DeathNote {
    return deathNoteRepo.save(deathNote)
  }
  
  override fun listNotebooks(): List<DeathNote> {
    return deathNoteRepo.findAll().toMutableList()
  }
  
}
