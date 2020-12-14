package com.muzammil.death_note_simulator

import org.hibernate.collection.spi.PersistentCollection
import org.modelmapper.Conditions
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
    return ModelMapper().also {
      it.configuration.propertyCondition = Conditions.isNotNull()
      // it.configuration.setPropertyCondition { context ->
      //   context.source !is PersistentCollection
      //   || (context.source as PersistentCollection).wasInitialized()
      // }
    }
  }
}
