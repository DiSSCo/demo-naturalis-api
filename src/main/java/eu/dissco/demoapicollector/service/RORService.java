package eu.dissco.demoapicollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.demoapicollector.client.RorClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RORService {

  private final RorClient client;
  private final ObjectMapper mapper;

  @Cacheable("rorid")
  public String getRoRId(String institutionCode) {
    log.info("Requesting ROR for organization: {}", institutionCode);
    var responseRor = client.requestOrganisationRor(institutionCode);
    try {
      var json = mapper.readTree(responseRor);
      var items = json.get("items");
      if(items.size() > 0) {
        var score = items.get(0).get("score");
        if (score.asDouble() == 1.0) {
          var rorId = items.get(0).get("organization").get("id");
          log.info("ROR for {} is {}", institutionCode, rorId);
          return rorId.asText();
        }
      }
    } catch (JsonProcessingException e) {
      log.error("Failed to retrieve a ROR id for: {}", institutionCode);
      return "";
    }
    log.warn("Could not match name to a ROR id for: {}", institutionCode);
    return "";
  }
}
