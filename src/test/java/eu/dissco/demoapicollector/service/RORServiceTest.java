package eu.dissco.demoapicollector.service;

import static eu.dissco.demoapicollector.TestUtils.INSTITUTION_NAME;
import static eu.dissco.demoapicollector.TestUtils.ROR_INSTITUTION;
import static eu.dissco.demoapicollector.TestUtils.loadResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.demoapicollector.client.RorClient;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RORServiceTest {

  private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
  @Mock
  private RorClient client;

  private RORService service;

  @BeforeEach
  void setup() {
    this.service = new RORService(client, mapper);
  }

  @Test
  void testGetRoRId() throws IOException {
    // Given
    given(client.requestOrganisationRor(anyString())).willReturn(
        loadResourceFile("ror-examples/naturalis-response.json"));

    // When
    var result = service.getRoRId(INSTITUTION_NAME);

    // Then
    assertThat(result).isEqualTo(ROR_INSTITUTION);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ror-examples/low-score-response.json",
      "ror-examples/empty-response.json", "ror-examples/invalid-response.json"})
  void testFailedRetrieval(String fileName) throws IOException {
    // Given
    given(client.requestOrganisationRor(anyString())).willReturn(
        loadResourceFile(fileName));

    // When
    var result = service.getRoRId(INSTITUTION_NAME);

    // Then
    assertThat(result).isEmpty();
  }

}
