package eu.dissco.demoapicollector.service;

import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.properties.CordraPropteries;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cnri.cordra.api.CordraClient;
import net.cnri.cordra.api.CordraException;
import net.cnri.cordra.api.CordraObject;
import net.cnri.cordra.api.CordraObject.Metadata;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CordraService {

  private final CordraClient cordra;
  private final CordraPropteries properties;

  public void saveItems(List<Authoritative> items) {
    for (Authoritative item : items) {
      var cordraObject = new CordraObject(properties.getType(), item);
      try {
        var response = cordra.create(cordraObject, properties.getDryrun());
        log.info("Successfully inserted object into cordra: {}", response.id);
      } catch (CordraException e) {
        log.error("Failed to insert OpenDS object: {}", cordraObject.getContentAsString(), e);
      }
    }
  }
}
