package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.exceptions.DataNotFoundException
import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.DeathNoteHistory
import com.muzammil.death_note_simulator.repos.deathnote.DeathNoteRepo
import com.muzammil.death_note_simulator.repos.deathnote_history.DeathNoteHistoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class DeathNoteService : IDeathNoteService {
  
  @Autowired
  lateinit var deathNoteRepo: DeathNoteRepo
  
  @Autowired
  lateinit var deathNoteHistoryRepo: DeathNoteHistoryRepo
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun createOrUpdateNotebook(deathNote: DeathNote): DeathNote {
    deathNote.owner?.let {
      deathNoteHistoryRepo.save(DeathNoteHistory(deathNote = deathNote,
                                                 owner = it))
    }
    return deathNoteRepo.save(deathNote)
  }
  
  @Transactional(readOnly = true)
  override fun findNotebook(deathNoteId: Long): DeathNote {
    return deathNoteRepo.findById(deathNoteId).orElse(null)
           ?: throw DataNotFoundException("No Death Note present with the given id")
  }
  
  @Transactional(readOnly = true)
  override fun listNotebooks(): List<DeathNote> {
    return deathNoteRepo.findAll().toMutableList()
  }
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  override fun deleteAll() {
    deathNoteRepo.deleteAll()
  }
  
  @Transactional
  override fun getOwnershipHistoryOf(notebookId: Long): List<DeathNoteHistory> {
    return deathNoteHistoryRepo.findAllByDeathNoteOrderById(findNotebook(notebookId))
  }
}
