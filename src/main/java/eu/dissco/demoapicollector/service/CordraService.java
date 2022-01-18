package eu.dissco.demoapicollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.properties.CordraPropteries;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dona.doip.client.AuthenticationInfo;
import net.dona.doip.client.DigitalObject;
import net.dona.doip.client.DoipClient;
import net.dona.doip.client.DoipException;
import net.dona.doip.client.ServiceInfo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CordraService {

  private final DoipClient cordra;
  private final AuthenticationInfo authenticationInfo;
  private final ServiceInfo serviceInfo;
  private final CordraPropteries properties;
  private final ObjectMapper mapper;

  public void saveItems(List<Authoritative> items) {
    for (Authoritative item : items) {
      var cordraObject = new DigitalObject();
      cordraObject.type = properties.getType();
      try {
        cordraObject.attributes = (JsonObject) JsonParser.parseString(mapper.writeValueAsString(item));
        var response = cordra.create(cordraObject, authenticationInfo, serviceInfo);
        log.info("Successfully inserted object into cordra: {}", response.id);
      } catch (DoipException | JsonProcessingException e) {
        log.error("Failed to insert OpenDS object: {}", cordraObject.attributes, e);
      }
    }
    cordra.close();
  }
}
