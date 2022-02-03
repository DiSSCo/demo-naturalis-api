package eu.dissco.demoapicollector.properties;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "cordra.rest")
@Profile("REST")
public class CordraRestProperties {

  @NotBlank
  private String host;

}
