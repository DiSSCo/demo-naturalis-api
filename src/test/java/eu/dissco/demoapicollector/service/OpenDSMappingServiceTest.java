package eu.dissco.demoapicollector.service;

import static eu.dissco.demoapicollector.TestUtils.ROR_INSTITUTION;
import static eu.dissco.demoapicollector.TestUtils.testAuthoritative;
import static eu.dissco.demoapicollector.TestUtils.testDarwin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenDSMappingServiceTest {

  @Mock
  private RORService rorService;

  private OpenDSMappingService service;

  @BeforeEach
  void setup() {
    this.service = new OpenDSMappingService(rorService);
  }

  @Test
  void testMapDarwinToOpenDS() {
    // Given
    given(rorService.getRoRId(anyString())).willReturn(ROR_INSTITUTION);

    // When
    var result = service.mapDarwinToOpenDS(List.of(testDarwin()));

    // When
    assertThat(result).isEqualTo(List.of(testAuthoritative()));
  }

}
