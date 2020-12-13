package com.muzammil.death_note_simulator.controllers.deathnote

import com.muzammil.death_note_simulator.models.DeathNote
import com.muzammil.death_note_simulator.models.dtos.DeathNoteDTO
import com.muzammil.death_note_simulator.models.dtos.DeathNoteExceptOwnerDTO
import com.muzammil.death_note_simulator.models.dtos.DeathNotesListDTO
import com.muzammil.death_note_simulator.services.deathnote.IDeathNoteService
import org.modelmapper.ModelMapper
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
  
  @Autowired
  lateinit var modelMapper: ModelMapper
  
  @PostMapping(value = ["create/{name}"])
  fun create(@PathVariable
             name: String): DeathNoteDTO {
    return deathNoteService
      .createOrUpdateNotebook(DeathNote(name = name))
      .let {
        modelMapper.map(it, DeathNoteDTO::class.java)
      }
  }
  
  @GetMapping(value = ["list"])
  fun list(): DeathNotesListDTO {
    return deathNoteService.listNotebooks().map {
      modelMapper.map(it, DeathNoteExceptOwnerDTO::class.java)
    }.let {
      DeathNotesListDTO(it)
    }
  }
  
  @GetMapping(value = ["{id}"])
  fun findById(@PathVariable
               id: Long): DeathNoteDTO {
    return deathNoteService.findNotebook(id).let {
      modelMapper.map(it, DeathNoteDTO::class.java)
    }
  }
}
