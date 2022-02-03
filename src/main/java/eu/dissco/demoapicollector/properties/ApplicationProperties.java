package eu.dissco.demoapicollector.properties;

import eu.dissco.demoapicollector.Actions;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  @NotNull
  private Actions action;

  @Positive
  private int numberOfObjects;

  @Positive
  private int numberOfRuns;

}
