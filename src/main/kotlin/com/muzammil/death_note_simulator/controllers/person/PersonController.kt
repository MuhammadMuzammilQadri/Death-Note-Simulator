package com.muzammil.death_note_simulator.controllers.person

import com.muzammil.death_note_simulator.models.Person
import com.muzammil.death_note_simulator.models.dtos.*
import com.muzammil.death_note_simulator.services.person.IPersonService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by Muzammil on 12/12/20.
 */
@RestController
@RequestMapping("person/")
class PersonController {
  
  @Autowired
  lateinit var personService: IPersonService
  
  @Autowired
  lateinit var modelMapper: ModelMapper
  
  @PostMapping(value = ["create"])
  fun createPerson(@RequestBody
                   body: PersonExceptIdDTO): PersonDTO {
    return modelMapper.map(body, Person::class.java).let {
      personService.savePerson(it).let {
        modelMapper.map(it, PersonDTO::class.java)
      }
    }
  }
  
  @GetMapping(value = ["list"])
  fun list(): PersonsListDTO {
    return PersonsListDTO().also {
      it.data = personService.listPersons().map {
        modelMapper.map(it, PersonDTO::class.java)
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
  
  @PutMapping(value = ["addfacestoseen/{id}"])
  fun addFacesToSeen(@PathVariable
                     id: Long,
                     @RequestBody
                     body: PersonIdListDTO): PersonWithFacesDTO {
    personService.getPersonById(id,
                                shouldFetchFaces = true).also { person ->
      person.facesSeen = person.facesSeen.toMutableSet().also {
        it.addAll(body.facesList.map {
          Person(it.id, name = "")
        })
      }
    }.let {
      return personService.savePerson(it).let {
        modelMapper.map(it, PersonWithFacesDTO::class.java)
      }
    }
  }
}
