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
  var name: String,
  var isAlive: Boolean = true,
  @ManyToMany(fetch = FetchType.EAGER)
  var facesSeen: List<Person> = mutableListOf()
                 ) {
}
