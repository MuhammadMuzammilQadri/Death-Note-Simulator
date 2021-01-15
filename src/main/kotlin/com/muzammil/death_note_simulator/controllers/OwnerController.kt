package com.muzammil.death_note_simulator.controllers

import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.dtos.*
import com.muzammil.death_note_simulator.helpers.authentication_facade.IAuthenticationFacade
import com.muzammil.death_note_simulator.services.owner.IOwnerService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
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
  
  @Autowired
  lateinit var authenticationFacade: IAuthenticationFacade
  
  
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
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(value = ["create"])
  fun create(@RequestBody
             body: CreateOwnerRequestDTO): OwnerWithFacesDTO {
    return ownerService.makeOwner(body.ownerId, body.deathNoteId).let {
      modelMapper.map(it, OwnerWithFacesDTO::class.java)
    }
  }
  
  @PutMapping(value = ["killperson"])
  fun killPerson(@RequestParam
                 personToKillId: Long) {
    authenticationFacade.getUser().id?.let {
      return ownerService.killPerson(it, personToKillId)
    } ?: throw UnknownException("Principle id not found while killing a person")
  }
  
  @GetMapping(value = ["killedpersonslist"])
  fun killedPersonsList(): PersonsListWithoutFacesDTO {
    authenticationFacade.getUser().id?.let { id ->
      return ownerService.getOwnerMemories(id).map { memory ->
        memory.killedPerson?.let {
          PersonDTO(it.id, it.name)
        } ?: throw UnknownException("Strange memory exists without a reference of killed person")
      }.let {
        PersonsListWithoutFacesDTO(it)
      }
    } ?: throw UnknownException("Principle id not found while retrieving killed persons list")
  }
}

