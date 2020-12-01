package com.muzammil.death_note_simulator.models

import javax.persistence.*

/**
 * Created by Muzammil on 11/29/20.
 */

@Entity
class Person(
  @GeneratedValue
  @Id
  var id: Long? = null,
  @Column(unique = true)
  var name: String,
  var isAlive: Boolean = true,
  @ManyToMany
  var facesSeen: Set<Person> = mutableSetOf(),
  @OneToMany(mappedBy = "owner")
  var deathNotes: Set<DeathNote> = mutableSetOf()
            ) {
}
