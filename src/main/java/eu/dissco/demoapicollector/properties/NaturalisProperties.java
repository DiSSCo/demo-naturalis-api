package eu.dissco.demoapicollector.properties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "naturalis")
public class NaturalisProperties {

  @Positive
  private int requestsize;
  @NotBlank
  private String field;
  @NotBlank
  private String operator;
  @NotBlank
  private String value;
}
