package eu.dissco.demoapicollector.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "naturalis", url = "https://api.biodiversitydata.nl/v2/")
public interface NaturalisClient {

  @GetMapping(value = "specimen/query/?_querySpec={query}")
  JsonNode getSpecimen(@PathVariable String query);

  @GetMapping(value = "multimedia/query/?_querySpec={query}")
  JsonNode getMultiMedia(@PathVariable String query);

}
