package com.muzammil.death_note_simulator.repos.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface DeathNoteRepo : CrudRepository<DeathNote, Long> {
  @Modifying
  @Query("UPDATE DeathNote d SET d.owner.id = :ownerId WHERE d.id = :deathNoteId")
  fun updateOwner(@Param("deathNoteId")
                  deathNoteId: Long,
                  @Param("ownerId")
                  ownerId: Long)
}
