package com.muzammil.death_note_simulator.security

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

/**
 * Created by Muzammil on 1/14/21.
 */
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  jsr250Enabled = true)
class MethodSecurityConfig : GlobalMethodSecurityConfiguration() {
}
