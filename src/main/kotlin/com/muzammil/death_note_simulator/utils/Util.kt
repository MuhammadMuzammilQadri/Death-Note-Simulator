package com.muzammil.death_note_simulator.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Muzammil on 11/14/20.
 */

inline fun <reified T> T.logger(): Logger {
  if (T::class.isCompanion) {
    return LoggerFactory.getLogger(T::class.java.enclosingClass)
  }
  return LoggerFactory.getLogger(T::class.java)
}
