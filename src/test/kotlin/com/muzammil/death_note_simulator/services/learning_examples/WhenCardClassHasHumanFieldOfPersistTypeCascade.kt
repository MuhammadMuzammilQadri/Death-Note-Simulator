package com.muzammil.death_note_simulator.services.learning_examples

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

/**
 * Created by Muzammil on 10/26/21.
 */

@Entity
class Human {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null;
  
  @Column
  var name: String? = null;
  
  @OneToMany(mappedBy = "human", fetch = FetchType.EAGER)
  var cardsList: List<Card> = emptyList()
  
  @PreRemove
  private fun clearCardsReferences() {
    cardsList.forEach {
      it.human = null
    }
  }
}

@Entity
class Card {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null;
  
  @ManyToOne(cascade = [CascadeType.PERSIST])
  @JoinColumn(name = "human_id")
  var human: Human? = null;
}


@Repository
interface CardRepo : CrudRepository<Card, Long> {
}

@Repository
interface HumanRepo : CrudRepository<Human, Long> {
}

@SpringBootTest
class WhenCardClassHasHumanFieldOfPersistTypeCascade {
  
  @Autowired
  lateinit var humanRepo: HumanRepo
  
  @Autowired
  lateinit var cardRepo: CardRepo
  
  lateinit var humanToBeSaved: Human
  
  @BeforeEach
  fun setup() {
    // delete everything
    humanRepo.deleteAll()
    cardRepo.deleteAll()
    
    humanToBeSaved = Human().apply {
      name = "Muzammil"
    }
    val card1 = Card().apply {
      human = humanToBeSaved
    }
    val card2 = Card().apply {
      human = humanToBeSaved
    }
    
    val card3 = Card().apply {
      human = humanToBeSaved
    }
    
    humanToBeSaved.cardsList = listOf(card1, card2, card3)
    
    // save cards along with their humans, because of cascading property on card.human
    cardRepo.saveAll(listOf(card1, card2, card3))
  }
  
  // because we don't have any cascading property on Human#cardsList
  @Test
  fun deletingHumanWontDeleteCards() {
    val fetchedHuman = humanRepo.findById(humanToBeSaved.id!!).get()
    
    // delete human from db
    humanRepo.delete(fetchedHuman)
    
    // make sure cards exist, but they don't point to Human anymore
    assertFalse(humanRepo.findById(humanToBeSaved.id!!).isPresent())
    assertEquals(0, humanRepo.count())
    val cardsAfterDeletingHuman = cardRepo.findAll()
    assertTrue(cardsAfterDeletingHuman.count() == 3)
    for (card in cardsAfterDeletingHuman) {
      assertNull(card.human)
    }
  }
  
  // because we don't have orphan removal property on Human#cardsList
  @Test
  fun deletingCardFromHumanWillNotDeleteItFromCardTable() {
    val humanToBeUpdated = humanRepo.findById(humanToBeSaved.id!!).get()
    
    // delete card from human, and update human
    humanRepo.save(humanToBeUpdated.apply {
      cardsList = cardsList.toMutableList().apply { removeLast() }
    })
    
    // make sure nothing is deleted,
    // as we tried to delete card from an entity (Human) that was NOT the Owner
    val updatedHuman = humanRepo.findById(humanToBeSaved.id!!)
    assertTrue(updatedHuman.isPresent)
    assertEquals(3, updatedHuman.get().cardsList.count())
    assertEquals(3, cardRepo.count())
  }
  
  
  // because we have only cascaded 'persist' type operations on Card#human
  @Test
  fun deletingACardWontDeleteHuman() {
    val fetchedHuman = humanRepo.findById(humanToBeSaved.id!!).get()
    
    // delete card from db
    cardRepo.delete(fetchedHuman.cardsList.get(0))
    
    // make sure it is deleted
    assertTrue(humanRepo.findById(humanToBeSaved.id!!).isPresent)
    assertEquals(2, cardRepo.count())
  }
}
