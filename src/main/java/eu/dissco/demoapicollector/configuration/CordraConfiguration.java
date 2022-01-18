package eu.dissco.demoapicollector.configuration;

import eu.dissco.demoapicollector.properties.CordraPropteries;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import lombok.AllArgsConstructor;
import net.dona.doip.client.AuthenticationInfo;
import net.dona.doip.client.DoipClient;
import net.dona.doip.client.PasswordAuthenticationInfo;
import net.dona.doip.client.ServiceInfo;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CordraConfiguration {

  private final CordraPropteries properties;

  @Bean
  DoipClient doipClient() {
    return new DoipClient();
  }

  @Bean
  AuthenticationInfo authenticationInfo() {
    return new PasswordAuthenticationInfo(properties.getUsername(), properties.getPassword());
  }

  @Bean
  ServiceInfo serviceInfo() {
    return new ServiceInfo("0.NA/20.5000.1025");
  }

}
