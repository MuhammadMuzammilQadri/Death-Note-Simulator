package com.muzammil.death_note_simulator.repos.person

import com.muzammil.death_note_simulator.models.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * Created by Muzammil on 11/29/20.
 */
interface UserRepo : CrudRepository<User, Long> {
  fun findByName(name: String): User?
  fun findAllByDeathNotesNotNull(): Set<User>
  fun findByIdAndDeathNotesNotNull(id: Long): User?
  
  @Modifying
  @Query("UPDATE User p SET p.isAlive = :isAlive WHERE p.id = :personToKillId")
  fun updateIsAliveStatus(@Param("personToKillId")
                          personToKillId: Long,
                          @Param("isAlive")
                          isAlive: Boolean)
  
  
  @Query("SELECT p FROM User p " +
         "INNER JOIN FETCH p.deathNotes dn " +
         "WHERE p.id = dn.owner.id")
  fun findOwners(): Set<User>
  
  @Query("SELECT p FROM User p " +
         "LEFT JOIN FETCH p.facesSeen fs " +
         "LEFT JOIN FETCH p.deathNotes dn " +
         "WHERE p.id = dn.owner.id")
  fun findOwnersWithFaces(): Set<User>
}
