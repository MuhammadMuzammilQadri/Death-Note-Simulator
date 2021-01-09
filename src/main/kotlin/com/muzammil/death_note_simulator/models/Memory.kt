package com.muzammil.death_note_simulator.models

import javax.persistence.*

/**
 * Created by Muzammil on 12/2/20.
 */
@Entity
class Memory(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "death_note_id")
  var deathNote: DeathNote? = null,
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner_id")
  var ownerPerson: User? = null,
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "person_id")
  var killedPerson: User? = null)
