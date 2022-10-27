package co.ouva.assignment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides LocalTime (which may be needed for "LocalTime" scalar type definition in the GQL schema)
 * and ObjectMapper beans. If you don't need these or require other beans/configurations, feel free
 * to modify anything you want.
 */
@Configuration
public class GraphQLConfig {

  @Bean
  public GraphQLScalarType localTimeScalar() {
    return ExtendedScalars.LocalTime;
  }

  @Bean
  public JavaTimeModule javaTimeModule() {
    return new JavaTimeModule();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(javaTimeModule());
    return mapper;
  }

}
