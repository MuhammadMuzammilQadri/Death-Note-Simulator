package com.muzammil.death_note_simulator.controllers

import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.User
import com.muzammil.death_note_simulator.models.dtos.*
import com.muzammil.death_note_simulator.helpers.authentication_facade.IAuthenticationFacade
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


/**
 * Created by Muzammil on 12/12/20.
 */
@RestController
@RequestMapping("person/")
@PreAuthorize("isAuthenticated()")
class PersonController {
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var modelMapper: ModelMapper
  
  @Autowired
  lateinit var authenticationFacade: IAuthenticationFacade
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(value = ["create"])
  fun createPerson(@RequestBody
                   body: PersonExceptIdDTO): PersonDTO {
    return modelMapper.map(body, User::class.java).let {
      personService.savePerson(it).let {
        modelMapper.map(it, PersonDTO::class.java)
      }
    }
  }
  
  @GetMapping(value = ["list"])
  fun list(@RequestParam(defaultValue = "false")
           withSeenFaces: Boolean): PersonsListDTO {
    return if (withSeenFaces) {
      PersonsListWithFacesDTO().also {
        it.data = personService.listPersons(true).map { p ->
          modelMapper.map(p, PersonWithFacesDTO::class.java)
        }
      }
    } else {
      PersonsListWithoutFacesDTO().also {
        it.data = personService.listPersons().map { p ->
          modelMapper.map(p, PersonDTO::class.java)
        }
      }
    }
  }
  
  @GetMapping(value = ["{id}"])
  fun getPerson(@PathVariable
                id: Long): PersonWithFacesDTO {
    return personService.getPersonById(id, shouldFetchFaces = true).let {
      modelMapper.map(it, PersonWithFacesDTO::class.java)
    }
  }
  
  @PutMapping(value = ["addfacestoseen/"])
  fun addFacesToSeen(@RequestParam(required = false)
                     id: Long,
                     @RequestBody
                     body: PersonIdListDTO): PersonWithFacesDTO {
    var selfIdOrOthers = id
    if (selfIdOrOthers < 0) {
      selfIdOrOthers = authenticationFacade.getUser().id
                       ?: throw UnknownException("Principle id not found while add faces to seen")
    }
    return personService
      .addFaceToPerson(selfIdOrOthers, body.facesList.map { it.id }.toTypedArray())
      .let {
        modelMapper.map(it, PersonWithFacesDTO::class.java)
      }
  }
}
