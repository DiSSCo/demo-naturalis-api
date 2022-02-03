package eu.dissco.demoapicollector;

import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.properties.ApplicationProperties;
import eu.dissco.demoapicollector.service.NaturalisService;
import eu.dissco.demoapicollector.service.OpenDSMappingService;
import eu.dissco.demoapicollector.service.cordra.CordraServiceInterface;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectRunner implements CommandLineRunner {

  private final OpenDSMappingService mappingService;
  private final CordraServiceInterface cordraService;
  private final NaturalisService naturalisService;
  private final ApplicationProperties properties;

  @Override
  public void run(String... args) {

    if (Actions.UPDATE == properties.getAction()) {
      runUpdateMode();
    } else if (Actions.CREATE == properties.getAction()) {
      runCreateMode();
    } else if (Actions.DELETE == properties.getAction()) {
      runDeleteMode();
    }
    log.info("Application successfully completed");
    System.exit(0);
  }

  private void runDeleteMode() {
    for (int i = 0; i < properties.getNumberOfRuns(); i++) {
      log.info("Collecting {} of id's", properties.getNumberOfObjects());
      var objects = cordraService.gatherIds(properties.getNumberOfObjects());
      log.info("Deleting {} of items", objects.size());
      var startCordraInsert = Instant.now();
      var futures = new ArrayList<CompletableFuture<Boolean>>();
      objects.forEach(digitalObject -> futures.add(cordraService.deleteItems(digitalObject)));
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      var totalTime = Instant.now().minusMillis(startCordraInsert.toEpochMilli()).toEpochMilli();
      log.info("Total time elapsed for deleting: {} records is {} milisec",
          objects.size(),
          totalTime);
    }
  }

  private void runUpdateMode() {
    for (int i = 0; i < properties.getNumberOfRuns(); i++) {
      log.info("Collecting {} of items", properties.getNumberOfObjects());
      var objects = cordraService.gatherObjects(properties.getNumberOfObjects());
      log.info("Updating {} of items", objects.size());

      var startCordraInsert = Instant.now();
      var futures = new ArrayList<CompletableFuture<Boolean>>();
      objects.forEach(digitalObject -> futures.add(cordraService.updateItems(digitalObject)));
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      var totalTime = Instant.now().minusMillis(startCordraInsert.toEpochMilli()).toEpochMilli();
      log.info("Total time elapsed for updating: {} records is {} milisec",
          objects.size(),
          totalTime);
    }
  }

  private void runCreateMode() {
    log.info("Starting retrieving data from naturalis API");
    var extractedRows = naturalisService.getNaturalisData(properties.getNumberOfObjects());
    log.info("Successfully parsed data to objects no. of rows found: {}", extractedRows.size());
    log.info("Starting mapping to OpenDS Object");
    var digitalSpecimen = mappingService.mapDarwinToOpenDS(extractedRows);
    log.info("Successfully mapped darwin to OpenDS, total Digital Specimen: {}",
        digitalSpecimen.size());
    for (int i = 0; i < properties.getNumberOfRuns(); i++) {
      log.info("Pushing items: {} to cordra", digitalSpecimen.size());
      var startCordraInsert = Instant.now();
      var futures = new ArrayList<CompletableFuture<Boolean>>();
      for (Authoritative item : digitalSpecimen) {
        futures.add(cordraService.saveItems(item));
      }
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      var totalTime = Instant.now().minusMillis(startCordraInsert.toEpochMilli()).toEpochMilli();
      log.info("Total time elapsed for inserting: {} records is {} milisec",
          digitalSpecimen.size(),
          totalTime);
    }
  }

}
