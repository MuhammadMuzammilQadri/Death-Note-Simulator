package com.muzammil.death_note_simulator.models

import javax.persistence.*

/**
 * Created by Muzammil on 11/29/20.
 */

@Entity
class DeathNoteHistory(
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null,
  
  @OneToOne
  @JoinColumn(name = "death_note_id")
  var deathNote: DeathNote? = null,
  
  @OneToOne
  @JoinColumn(name = "owner_id")
  var owner: User? = null)
