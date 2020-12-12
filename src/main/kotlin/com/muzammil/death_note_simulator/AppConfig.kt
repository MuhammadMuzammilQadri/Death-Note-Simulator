package com.muzammil.death_note_simulator

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by Muzammil on 12/12/20.
 */
@Configuration
class AppConfig {
  
  @Bean
  fun modelMapper(): ModelMapper {
    return ModelMapper()
  }
}
