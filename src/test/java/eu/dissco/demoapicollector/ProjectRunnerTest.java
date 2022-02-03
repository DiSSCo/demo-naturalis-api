package eu.dissco.demoapicollector;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;

import eu.dissco.demoapicollector.properties.ApplicationProperties;
import eu.dissco.demoapicollector.service.cordra.CordraRestService;
import eu.dissco.demoapicollector.service.NaturalisService;
import eu.dissco.demoapicollector.service.OpenDSMappingService;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectRunnerTest {

  @Mock
  private OpenDSMappingService mappingService;

  @Mock
  private CordraRestService cordraService;

  @Mock
  private NaturalisService naturalisService;

  @Mock
  private ApplicationProperties propteries;

  private ProjectRunner runner;

  @BeforeEach
  void setup() {
    this.runner = new ProjectRunner(mappingService, cordraService, naturalisService, propteries);
  }

  @Test
  void testRun() throws ExecutionException, InterruptedException {
    // When
    runner.run();

    // Then
    then(naturalisService).should().getNaturalisData(anyInt());
    then(mappingService).should().mapDarwinToOpenDS(anyList());
    then(cordraService).should().saveItems(any());
  }



}
