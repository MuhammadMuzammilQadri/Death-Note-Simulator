package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.DeathNoteHistory
import com.muzammil.death_note_simulator.models.User

/**
 * Created by Muzammil on 11/29/20.
 */
interface IDeathNoteService {
  fun createOrUpdateNotebook(deathNote: DeathNote): DeathNote
  fun makeOwner(deathNote: DeathNote, owner: User): DeathNote
  fun makeOwner(deathNoteId: Long, ownerId: Long): DeathNote
  fun listNotebooks(): List<DeathNote>
  fun findNotebook(deathNoteId: Long): DeathNote
  fun deleteAll()
  fun getOwnershipHistoryOf(notebookId: Long): List<DeathNoteHistory>
}
