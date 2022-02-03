package eu.dissco.demoapicollector.configuration;

import eu.dissco.demoapicollector.properties.CordaDOIPProperties;
import eu.dissco.demoapicollector.properties.CordraProperties;
import lombok.AllArgsConstructor;
import net.dona.doip.client.AuthenticationInfo;
import net.dona.doip.client.PasswordAuthenticationInfo;
import net.dona.doip.client.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@AllArgsConstructor
@Profile("DOIP")
public class CordraDOIPConfiguration {

  private final CordaDOIPProperties properties;
  private final CordraProperties cordraProperties;

  @Bean
  AuthenticationInfo authenticationInfo() {
    return new PasswordAuthenticationInfo(cordraProperties.getUsername(),
        cordraProperties.getPassword());
  }

  @Bean
  ServiceInfo serviceInfo() {
    return new ServiceInfo(properties.getServiceId(), properties.getIp(), properties.getDoipPort());
  }

}
