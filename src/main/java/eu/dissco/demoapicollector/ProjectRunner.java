package eu.dissco.demoapicollector;

import eu.dissco.demoapicollector.service.CordraService;
import eu.dissco.demoapicollector.service.NaturalisService;
import eu.dissco.demoapicollector.service.OpenDSMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectRunner implements CommandLineRunner {

  private final OpenDSMappingService mappingService;
  private final CordraService cordraService;
  private final NaturalisService naturalisService;

  @Override
  public void run(String... args) {
    log.info("Starting retrieving data from naturalis API");
    var extractedRows = naturalisService.getNaturalisData();
    log.info("Successfully parsed data to objects no. of rows found: {}", extractedRows.size());
    log.info("Starting mapping to OpenDS Object");
    var digitalSpecimen = mappingService.mapDarwinToOpenDS(extractedRows);
    log.info("Successfully mapped darwin to OpenDS, total Digital Specimen: {}",
        digitalSpecimen.size());
    log.info("Pushing items: {} to cordra", digitalSpecimen.size());
    cordraService.saveItems(digitalSpecimen);
    log.info("Application successfully completed");
  }

}
