package eu.dissco.demoapicollector.service.cordra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.properties.CordraProperties;
import eu.dissco.demoapicollector.properties.CordraRestProperties;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cnri.cordra.api.CordraClient;
import net.cnri.cordra.api.CordraException;
import net.cnri.cordra.api.CordraObject;
import net.cnri.cordra.api.QueryParams;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@Profile("REST")
public class CordraRestService implements CordraServiceInterface {

  private static final String MIDSLEVEL = "midslevel";

  private final CordraClient cordraClient;
  private final CordraProperties cordraProperties;
  private final ObjectMapper mapper;

  @Async
  public CompletableFuture<Boolean> saveItems(Authoritative item) {
    try {
      var cordraObject = new CordraObject(cordraProperties.getType(), mapper.writeValueAsString(item));
      var response = cordraClient.create(cordraObject);
      log.debug("Successfully inserted object into cordra via Rest: {}", response.id);
      return CompletableFuture.completedFuture(true);
    } catch (CordraException | JsonProcessingException e) {
      log.error("Failed to insert OpenDS object: {}", item, e);
      return CompletableFuture.completedFuture(false);
    }
  }

  @Override
  @Async
  public CompletableFuture<Boolean> updateItems(Object genericObject) {
    var object = (CordraObject) genericObject;
    var content = object.content.getAsJsonObject();
    var count = 0;
    if (content.has(MIDSLEVEL)) {
      count = content.get(MIDSLEVEL).getAsInt() + 1;
      content.remove(MIDSLEVEL);
    }
    content.addProperty(MIDSLEVEL, count);
    var digitalObject = new CordraObject();
    digitalObject.id = object.id;
    digitalObject.type = object.type;
    digitalObject.setContent(content);
    try {
      var updatedObject = cordraClient.update(digitalObject);
      log.debug("Updated object with id: {}", updatedObject.id);
      return CompletableFuture.completedFuture(true);
    } catch (CordraException e) {
      e.printStackTrace();
    }
    return CompletableFuture.completedFuture(false);
  }

  @Override
  public List<Object> gatherObjects(int numberOfObjects) {
    try {
      var result = cordraClient.search("type:\"" + cordraProperties.getType() + "\"",
          new QueryParams(0, numberOfObjects));
      return result.stream().collect(Collectors.toList());
    } catch (CordraException e) {
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public List<String> gatherIds(int numberOfObjects) {
    try {
      var result = cordraClient.searchHandles("type:\"" + cordraProperties.getType() + "\"",
          new QueryParams(0, numberOfObjects));
      return result.stream().collect(Collectors.toList());
    } catch (CordraException e) {
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  @Async
  public CompletableFuture<Boolean> deleteItems(String digitalObject) {
    try {
      cordraClient.delete(digitalObject);
      log.debug("Deleted object with id: {}", digitalObject);
      return CompletableFuture.completedFuture(true);
    } catch (CordraException e) {
      e.printStackTrace();
      return CompletableFuture.completedFuture(false);
    }
  }

}
