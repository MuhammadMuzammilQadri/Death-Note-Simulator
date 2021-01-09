package com.muzammil.death_note_simulator.repos.deathnote_history

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.DeathNoteHistory
import com.muzammil.death_note_simulator.models.User
import org.springframework.data.repository.CrudRepository

/**
 * Created by Muzammil on 11/29/20.
 */
interface DeathNoteHistoryRepo : CrudRepository<DeathNoteHistory, Long> {
  fun findAllByOwnerOrderById(ownerPerson: User): List<DeathNoteHistory>
  fun findAllByDeathNoteOrderById(deathNote: DeathNote): List<DeathNoteHistory>
}
