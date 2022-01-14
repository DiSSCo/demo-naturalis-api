package eu.dissco.demoapicollector.configuration;

import eu.dissco.demoapicollector.properties.CordraPropteries;
import lombok.AllArgsConstructor;
import net.cnri.cordra.api.CordraClient;
import net.cnri.cordra.api.CordraException;
import net.cnri.cordra.api.TokenUsingHttpCordraClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CordraConfiguration {

  private final CordraPropteries properties;

  @Bean
  CordraClient cordraClient() throws CordraException {
    return new TokenUsingHttpCordraClient(properties.getHost(), properties.getUsername(),
        properties.getPassword());
  }
}
