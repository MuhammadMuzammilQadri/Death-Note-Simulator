package com.muzammil.death_note_simulator.security

/**
 * Created by Muzammil on 1/9/21.
 */

import com.muzammil.death_note_simulator.exceptions.UnknownException
import com.muzammil.death_note_simulator.models.MyUserDetails
import com.muzammil.death_note_simulator.models.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtil {
  private val SECRET_KEY = "c2VjcmV0"
  val ROLES = "roles"
  val ID = "id"
  
  fun extractUsername(token: String?): String {
    return extractClaim(token, Claims::getSubject)
  }
  
  fun extractExpiration(token: String?): Date {
    return extractClaim(token, Claims::getExpiration)
  }
  
  fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T {
    val claims: Claims = extractAllClaims(token)
    return claimsResolver(claims)
  }
  
  private fun extractAllClaims(token: String?): Claims {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody()
  }
  
  private fun isTokenExpired(token: String?): Boolean {
    return extractExpiration(token).before(Date())
  }
  
  fun generateToken(userDetails: MyUserDetails): String {
    val claims: Map<String, Any> = mutableMapOf<String, Any>().also {
      it.put(ROLES,
             userDetails.authorities?.joinToString()
             ?: throw UnknownException("Invalid authorities while creating token"))
      it.put(ID, userDetails.user.id?.toString()
                 ?: throw UnknownException("Invalid id while creating token"))
    }
    return createToken(claims, userDetails.username)
  }
  
  fun parseToken(token: String): UserDetails {
    val claims: Claims = extractAllClaims(token)
    return User(id = claims.get(ID, String::class.java).toLong(),
                name = claims.subject,
                roles = enumValueOf(claims.get(ROLES, String::class.java)))
      .let { MyUserDetails(it) }
  }
  
  private fun createToken(claims: Map<String, Any>,
                          subject: String): String {
    return Jwts.builder().setClaims(claims).setSubject(subject)
      .setIssuedAt(Date(System.currentTimeMillis()))
      .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()
  }
}
