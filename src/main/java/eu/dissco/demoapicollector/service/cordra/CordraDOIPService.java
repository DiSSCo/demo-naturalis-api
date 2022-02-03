package eu.dissco.demoapicollector.service.cordra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.properties.CordaDOIPProperties;
import eu.dissco.demoapicollector.properties.CordraProperties;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dona.doip.client.AuthenticationInfo;
import net.dona.doip.client.DigitalObject;
import net.dona.doip.client.DoipClient;
import net.dona.doip.client.DoipException;
import net.dona.doip.client.QueryParams;
import net.dona.doip.client.ServiceInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@Profile("DOIP")
public class CordraDOIPService implements CordraServiceInterface {

  private static final String CONTENT = "content";
  private static final String MIDSLEVEL = "midslevel";

  private final AuthenticationInfo authenticationInfo;
  private final ServiceInfo serviceInfo;
  private final CordraProperties cordraProperties;
  private final ObjectMapper mapper;

  @Async
  public CompletableFuture<Boolean> saveItems(Authoritative item) {
    var cordraObject = new DigitalObject();
    cordraObject.type = cordraProperties.getType();
    try (var cordra = new DoipClient()) {
      cordraObject.setAttribute(CONTENT,
          JsonParser.parseString(mapper.writeValueAsString(item)));
      var response = cordra.create(cordraObject, authenticationInfo, serviceInfo);
      log.debug("Successfully inserted object into cordra via DOIP: {}", response.id);
      return CompletableFuture.completedFuture(true);
    } catch (DoipException | JsonProcessingException e) {
      log.error("Failed to insert OpenDS object: {}", cordraObject.attributes, e);
      return CompletableFuture.completedFuture(false);
    }
  }

  @Override
  @Async
  public CompletableFuture<Boolean> updateItems(Object genericObject) {
    var object = (DigitalObject) genericObject;
    var content = object.attributes.get(CONTENT).getAsJsonObject();
    var count = 0;
    if (content.has(MIDSLEVEL)) {
      count = content.get(MIDSLEVEL).getAsInt() + 1;
      content.remove(MIDSLEVEL);
    }
    content.addProperty(MIDSLEVEL, count);
    var digitalObject = new DigitalObject();
    digitalObject.id = object.id;
    digitalObject.type = object.type;
    digitalObject.setAttribute(CONTENT, content);
    try (var cordra = new DoipClient()) {
      var updatedObject = cordra.update(digitalObject, authenticationInfo, serviceInfo);
      log.debug("Updated object with id: {}", updatedObject.id);
      return CompletableFuture.completedFuture(true);
    } catch (DoipException e) {
      e.printStackTrace();
    }
    return CompletableFuture.completedFuture(false);
  }

  @Override
  public List<Object> gatherObjects(int numberOfObjects) {
    try (var cordra = new DoipClient()) {
      var result = cordra.search(serviceInfo.serviceId, "type:\"" + cordraProperties.getType() + "\"",
          new QueryParams(0, numberOfObjects), authenticationInfo, serviceInfo);
      return result.stream().collect(Collectors.toList());
    } catch (DoipException ex) {
      ex.printStackTrace();
      return List.of();
    }
  }

  @Override
  public List<String> gatherIds(int numberOfObjects) {
    try (var cordra = new DoipClient()) {
      var result = cordra.searchIds(serviceInfo.serviceId, "type:\"" + cordraProperties.getType() + "\"",
          new QueryParams(0, numberOfObjects), authenticationInfo, serviceInfo);
      return result.stream().collect(Collectors.toList());
    } catch (DoipException ex) {
      ex.printStackTrace();
      return List.of();
    }
  }

  @Override
  @Async
  public CompletableFuture<Boolean> deleteItems(String digitalObject) {
    try(var cordra = new DoipClient()) {
      cordra.delete(digitalObject, authenticationInfo, serviceInfo);
      log.debug("Deleted object with id: {}", digitalObject);
      return CompletableFuture.completedFuture(true);
    } catch (DoipException e) {
      log.error("Failed to delete id: {}", digitalObject, e);
      return CompletableFuture.completedFuture(false);
    }
  }

}
