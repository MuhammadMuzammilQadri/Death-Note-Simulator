package com.muzammil.death_note_simulator.filters

import com.muzammil.death_note_simulator.config.JwtUtil
import com.muzammil.death_note_simulator.services.userdetail.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Muzammil on 1/11/21.
 */
@Component
class JwtRequestFilter : OncePerRequestFilter() {
  
  @Autowired
  lateinit var userDetailService: MyUserDetailService
  
  @Autowired
  lateinit var jwtUtil: JwtUtil
  
  override fun doFilterInternal(request: HttpServletRequest,
                                response: HttpServletResponse,
                                filterChain: FilterChain) {
    request.getHeader("Authorization")
      ?.takeIf { it.startsWith("Bearer ") }
      ?.substring("Bearer ".length)
      ?.takeIf { SecurityContextHolder.getContext().authentication == null }
      ?.let { token ->
        jwtUtil.parseToken(token).let { userDetails ->
          SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
              .also {
                it.details = WebAuthenticationDetailsSource().buildDetails(request)
              }
        }
      }
    
    filterChain.doFilter(request, response)
  }
}
