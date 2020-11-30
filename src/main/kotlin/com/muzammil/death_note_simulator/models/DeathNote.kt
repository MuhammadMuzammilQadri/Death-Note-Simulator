package com.muzammil.death_note_simulator.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * Created by Muzammil on 11/29/20.
 */

@Entity
class DeathNote(
  @GeneratedValue
  @Id
  var id: Long? = null,
  var name: String,
  @ManyToOne
  var owner: Person? = null
               ) {
  
}
