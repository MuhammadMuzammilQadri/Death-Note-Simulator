package com.muzammil.death_note_simulator.services.deathnote

import com.muzammil.death_note_simulator.models.DeathNote

/**
 * Created by Muzammil on 11/29/20.
 */
interface IDeathNoteService {
  fun createOrUpdateNotebook(deathNote: DeathNote): DeathNote
  fun listNotebooks(): List<DeathNote>
  fun findNotebook(deathNoteId: Long): DeathNote?
  fun deleteAll()
}
