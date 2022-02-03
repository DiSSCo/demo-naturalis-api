package eu.dissco.demoapicollector.configuration;

import eu.dissco.demoapicollector.properties.CordraProperties;
import eu.dissco.demoapicollector.properties.CordraRestProperties;
import lombok.AllArgsConstructor;
import net.cnri.cordra.api.CordraClient;
import net.cnri.cordra.api.CordraException;
import net.cnri.cordra.api.TokenUsingHttpCordraClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@AllArgsConstructor
@Profile("REST")
public class CordraRestConfiguration {

  private final CordraRestProperties properties;
  private final CordraProperties cordraProperties;

  @Bean
  CordraClient cordraClient() throws CordraException {
    return new TokenUsingHttpCordraClient(properties.getHost(), cordraProperties.getUsername(),
        cordraProperties.getPassword());
  }

}
