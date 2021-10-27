package com.muzammil.death_note_simulator.services.learning_examples

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.*

/**
 * Created by Muzammil on 10/26/21.
 */

@Entity
class Post {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null;
  
  @Column
  var content: String? = null;
  
  @OneToOne(mappedBy = "post", cascade = [CascadeType.ALL])
  var postDetails: PostDetails? = null
  
}

@Entity
class PostDetails {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  var id: Long? = null;
  
  @OneToOne
  @JoinColumn(name = "post_id")
  var post: Post? = null
  
}



@Repository
interface PostRepo : CrudRepository<Post, Long> {
}

@Repository
interface PostDetailsRepo : CrudRepository<PostDetails, Long> {
}

@SpringBootTest
class WhenPostHasPostDetailsFieldOfAllTypeCascade {
  
  @Autowired
  lateinit var postRepo: PostRepo
  
  @Autowired
  lateinit var postDetailsRepo: PostDetailsRepo
  
  lateinit var post: Post
  
  @BeforeEach
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun setup() {
    // delete everything
    postRepo.deleteAll()
    postDetailsRepo.deleteAll()
    
    val pd = PostDetails();
    post = Post().apply {
      content = "A dummy post content"
      postDetails = pd
    }
    
    postRepo.save(post)
    
    assertTrue(postRepo.count() > 0L)
    assertTrue(postDetailsRepo.count() > 0L)
  }
  
  // because we have cascaded all operations on Post#postDetails
  @Test
  fun deletingPostWillDeleteBothPostAndPostDetails() {
    postRepo.delete(post)
    
    assertTrue(postRepo.count() == 0L)
    assertTrue(postDetailsRepo.count() == 0L)
  }
  
  // because we are not doing any cascading on PostDetail#post
  @Test
  fun deletingPostDetailsWillNotDeletePost() {
    postDetailsRepo.delete(post.postDetails!!)
    
    assertTrue(postRepo.count() == 1L)
    assertTrue(postDetailsRepo.count() == 0L)
  }
}
