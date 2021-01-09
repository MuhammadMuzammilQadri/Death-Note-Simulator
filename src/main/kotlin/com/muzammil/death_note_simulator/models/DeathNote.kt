package com.muzammil.death_note_simulator.models

import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Created by Muzammil on 11/29/20.
 */

@Entity
class DeathNote(
  @GeneratedValue
  @Id
  var id: Long? = null,
  
  @Column(unique = true, nullable = false)
  @field:NotNull
  @field:NotEmpty(message = "name cannot be empty")
  var name: String? = null,
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", unique = true)
  var owner: User? = null)
