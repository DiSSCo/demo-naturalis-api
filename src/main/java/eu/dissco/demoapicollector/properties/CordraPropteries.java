package eu.dissco.demoapicollector.properties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cordra")
public class CordraPropteries {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
  @NotBlank
  private String host;
  @NotNull
  private Boolean dryrun;
  @NotBlank
  private String type;
}
