package com.muzammil.death_note_simulator.controllers.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by Muzammil on 12/6/20.
 */
@RestController
@RequestMapping("deathnote/")
class DeathNoteController {
  
  @Autowired
  lateinit var deathNoteService: IDeathNoteService
  
  @PostMapping(value = ["create/{name}"])
  fun create(@PathVariable
             name: String): DeathNote {
    return deathNoteService.createOrUpdateNotebook(DeathNote(name = name))
  }
  
  @GetMapping(value = ["list"])
  fun list() : List<DeathNote> {
    val notebooks = deathNoteService.listNotebooks()
    return notebooks
  }
  
  @GetMapping(value = ["{id}"])
  fun findById(@PathVariable id: Long) : DeathNote {
    return deathNoteService.findNotebook(id)
  }
}
