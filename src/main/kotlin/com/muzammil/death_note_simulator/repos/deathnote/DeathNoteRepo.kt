package com.muzammil.death_note_simulator.repos.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import org.springframework.data.repository.CrudRepository

interface DeathNoteRepo : CrudRepository<DeathNote, Long>{
}
