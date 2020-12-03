package com.muzammil.death_note_simulator.models

import javax.persistence.*

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
  @JoinColumn(name = "owner_id", unique = true)
  var owner: Person? = null)
