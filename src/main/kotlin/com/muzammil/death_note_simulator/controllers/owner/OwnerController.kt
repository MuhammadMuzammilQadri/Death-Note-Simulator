package com.muzammil.death_note_simulator.controllers.owner

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
           withseenfaces: Boolean,
           @RequestParam(defaultValue = "false")
           withdeathnotes: Boolean): PersonsListDTO {
    return if (withseenfaces || withdeathnotes) {
      OwnersListWithFacesDTO().also {
        it.data = ownerService
          .listOwners(shouldFetchFaces = withseenfaces,
                      shouldFetchDeathNotes = withdeathnotes)
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
}
