package eu.dissco.demoapicollector;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.then;

import eu.dissco.demoapicollector.service.CordraService;
import eu.dissco.demoapicollector.service.NaturalisService;
import eu.dissco.demoapicollector.service.OpenDSMappingService;
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
  private CordraService cordraService;

  @Mock
  private NaturalisService naturalisService;

  private ProjectRunner runner;

  @BeforeEach
  void setup() {
    this.runner = new ProjectRunner(mappingService, cordraService, naturalisService);
  }

  @Test
  void testRun() {
    // When
    runner.run();

    // Then
    then(naturalisService).should().getNaturalisData();
    then(mappingService).should().mapDarwinToOpenDS(anyList());
    then(cordraService).should().saveItems(anyList());
  }



}
