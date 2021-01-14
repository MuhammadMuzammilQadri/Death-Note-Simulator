package com.muzammil.death_note_simulator.controllers

import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.dtos.*
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by Muzammil on 12/14/20.
 */
@RestController
@RequestMapping("owner/")
class OwnerController {
  
  @Autowired
  lateinit var ownerService: IOwnerService
  
  @Autowired
  lateinit var modelMapper: ModelMapper
  
  
  @GetMapping(value = ["list"])
  fun list(@RequestParam(defaultValue = "false")
           withSeenFaces: Boolean,
           @RequestParam(defaultValue = "false")
           withDeathNotes: Boolean): PersonsListDTO {
    return if (withSeenFaces || withDeathNotes) {
      OwnersListWithFacesDTO().also {
        it.data = ownerService
          .listOwners(shouldFetchFaces = withSeenFaces)
          .map { p ->
            modelMapper.map(p, OwnerWithFacesDTO::class.java)
          }
      }
    } else {
      PersonsListWithoutFacesDTO().also {
        it.data = ownerService.listOwners().map { p ->
          modelMapper.map(p, PersonDTO::class.java)
        }
      }
    }
  }
  
  @PostMapping(value = ["create"])
  fun create(@RequestBody
             body: CreateOwnerRequestDTO): OwnerWithFacesDTO {
    return ownerService.makeOwner(body.ownerId, body.deathNoteId).let {
      modelMapper.map(it, OwnerWithFacesDTO::class.java)
    }
  }
  
  @PutMapping(value = ["killperson"])
  fun killPerson(@RequestParam
                 ownerId: Long,
                 @RequestParam
                 personToKillId: Long) {
    return ownerService.killPerson(ownerId, personToKillId)
  }
  
  @GetMapping(value = ["killedpersonslist"])
  fun killedPersonsList(@RequestParam
                        ownerId: Long): PersonsListWithoutFacesDTO {
    return ownerService.getOwnerMemories(ownerId).map { memory ->
      memory.killedPerson?.let {
        PersonDTO(it.id, it.name)
      } ?: throw UnknownException("Strange memory exists without a reference of killed person")
    }.let {
      PersonsListWithoutFacesDTO(it)
    }
  }
}

