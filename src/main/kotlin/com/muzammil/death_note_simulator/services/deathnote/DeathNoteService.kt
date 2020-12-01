package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.repos.deathnote.DeathNoteRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class DeathNoteService : IDeathNoteService {
  
  @Autowired
  lateinit var deathNoteRepo: DeathNoteRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun createOrUpdateNotebook(deathNote: DeathNote): DeathNote {
    return deathNoteRepo.save(deathNote)
  }
  
  @Transactional(readOnly = true)
  override fun findNotebook(deathNoteId: Long): DeathNote? {
    return deathNoteRepo.findById(deathNoteId).get()
  }
  
  @Transactional(readOnly = true)
  override fun listNotebooks(): List<DeathNote> {
    return deathNoteRepo.findAll().toMutableList()
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    deathNoteRepo.deleteAll()
  }
  
}
