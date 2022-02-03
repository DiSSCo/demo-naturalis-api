package eu.dissco.demoapicollector.properties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "cordra.doip")
@Profile("DOIP")
@Validated
public class CordaDOIPProperties {

  @NotNull
  private String serviceId;
  @NotBlank
  private String ip;
  @Positive
  private int doipPort;

}
