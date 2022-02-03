package eu.dissco.demoapicollector.properties;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("cordra")
public class CordraProperties {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String type;

}
