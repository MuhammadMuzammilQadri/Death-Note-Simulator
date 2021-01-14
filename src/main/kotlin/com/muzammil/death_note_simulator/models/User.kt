package com.muzammil.death_note_simulator.models

import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Created by Muzammil on 11/29/20.
 */

@Entity
class User(
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null,
  
  @Column(unique = true, nullable = false)
  @field:NotNull
  @field:NotEmpty(message = "name cannot be empty")
  var name: String? = null,
  
  var isAlive: Boolean = true,
  
  var password: String = "123",

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  var roles: AppRole = AppRole.USER,
  
  @ManyToMany
  var facesSeen: Set<User>? = mutableSetOf(),
  
  @OneToMany(mappedBy = "owner")
  var deathNotes: Set<DeathNote>? = mutableSetOf())
