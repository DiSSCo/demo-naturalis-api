package eu.dissco.demoapicollector.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.dissco.demoapicollector.client.NaturalisClient;
import eu.dissco.demoapicollector.domain.DarwinCore;
import eu.dissco.demoapicollector.domain.Image;
import eu.dissco.demoapicollector.properties.NaturalisProperties;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class NaturalisService {

  private static final String RESULT_SET = "resultSet";
  private static final String CONDITIONS = "conditions";
  private static final String FIELD = "field";
  private static final String OPERATOR = "operator";
  private static final String VALUE = "value";

  private final NaturalisClient client;
  private final NaturalisProperties properties;

  public List<DarwinCore> getNaturalisData() {
    log.info("Getting data from Naturalis API");
    var query = buildSpecimenQuery();
    log.info("With query: {}", query);
    var result = client.getSpecimen(URLEncoder.encode(query, StandardCharsets.UTF_8));
    var parsedObjects = new ArrayList<DarwinCore>();
    for (var node : result.get(RESULT_SET)) {
      parseJson(parsedObjects, node);
    }
    log.info("Result from Naturalis API is: {}", parsedObjects.size());
    return parsedObjects;
  }

  private void parseJson(ArrayList<DarwinCore> parsedObjects, JsonNode node) {
    var item = node.get("item");
    var images = gatherImages(item);
    var parsedObject = DarwinCore.builder()
        .basisOfRecord(item.get("recordBasis").asText())
        .institutionID(item.get("owner").asText())
        .occurrenceID(item.get("unitGUID").asText())
        .catalogNumber(item.get("sourceSystemId").asText())
        .id(item.get("id").asText())
        .scientificName(
            item.get("identifications").get(0).get("scientificName").get("scientificNameGroup")
                .asText())
        .images(images.isEmpty() ? null : images)
        .build();
    parsedObjects.add(parsedObject);
  }

  private List<Image> gatherImages(JsonNode item) {
    var images = new ArrayList<Image>();
    if (item.has("associatedMultiMediaUris")) {
      log.info("Found associated media for item: {}", item.get("id").asText());
      for (JsonNode image : item.get("associatedMultiMediaUris")) {
        images.add(Image.builder()
            .imageUrl(image.get("accessUri").asText())
            .format(image.get("format").asText())
            .variant(image.get("variant").asText())
            .build());
      }
    }
    return images;
  }

  private String buildSpecimenQuery() {
    var node = new JsonObject();
    node.addProperty("size", properties.getRequestsize());
    var condition = new JsonObject();
    condition.addProperty(FIELD, properties.getField());
    condition.addProperty(OPERATOR, properties.getOperator());
    condition.addProperty(VALUE, properties.getValue());
    var conditionsArray = new JsonArray();
    conditionsArray.add(condition);
    node.add(CONDITIONS, conditionsArray);
    return node.toString();
  }

}
