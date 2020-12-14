package com.muzammil.death_note_simulator.repos.person

import com.muzammil.death_note_simulator.models.Person
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * Created by Muzammil on 11/29/20.
 */
interface PersonRepo : CrudRepository<Person, Long> {
  fun findByName(name: String): Person?
  fun findAllByDeathNotesNotNull(): Set<Person>
  fun findByIdAndDeathNotesNotNull(id: Long): Person?
  
  @Modifying
  @Query("UPDATE Person p SET p.isAlive = :isAlive WHERE p.id = :personToKillId")
  fun updateIsAliveStatus(@Param("personToKillId")
                          personToKillId: Long,
                          @Param("isAlive")
                          isAlive: Boolean)
}
