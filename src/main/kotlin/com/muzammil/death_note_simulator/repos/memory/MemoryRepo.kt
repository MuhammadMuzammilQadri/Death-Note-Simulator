package com.muzammil.death_note_simulator.repos.memory

import com.muzammil.death_note_simulator.models.Memory
import com.muzammil.death_note_simulator.models.Person
import org.springframework.data.repository.CrudRepository

/**
 * Created by Muzammil on 11/29/20.
 */
interface MemoryRepo : CrudRepository<Memory, Long> {
  fun findAllByOwnerPersonOrderById(ownerPerson: Person): List<Memory>
}
