package eu.dissco.demoapicollector.service;

import static eu.dissco.demoapicollector.TestUtils.INSTITUTION_NAME;
import static eu.dissco.demoapicollector.TestUtils.loadResourceFile;
import static eu.dissco.demoapicollector.TestUtils.testDarwin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.demoapicollector.client.NaturalisClient;
import eu.dissco.demoapicollector.domain.DarwinCore;
import eu.dissco.demoapicollector.properties.NaturalisProperties;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NaturalisServiceTest {

  @Mock
  private NaturalisClient client;

  @Mock
  private NaturalisProperties properties;

  private NaturalisService service;

  private ObjectMapper mapper;

  @BeforeEach
  void setup() {
    this.service = new NaturalisService(client, properties);
    mapper = new ObjectMapper().findAndRegisterModules();
  }

  @Test
  void testGetNaturalisData() throws IOException {
    // Given
    given(properties.getRequestsize()).willReturn(2);
    given(properties.getField()).willReturn("kindOfUnit");
    given(properties.getOperator()).willReturn("EQUALS_IC");
    given(properties.getValue()).willReturn("nest");
    given(client.getSpecimen(anyString())).willReturn(
        mapper.readTree(loadResourceFile("naturalis/valid-specimen-example.json")));
    var expected = expectedResult();

    // When
    var result = service.getNaturalisData();

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private List<DarwinCore> expectedResult() {
    var withoutImages = DarwinCore.builder()
        .images(null)
        .id("ZMA.AVES.64706@CRS")
        .occurrenceID("https://data.biodiversitydata.nl/naturalis/specimen/ZMA.AVES.64706")
        .catalogNumber("ZMA.AVES.64706")
        .basisOfRecord("PreservedSpecimen")
        .scientificName("aegithinia tiphia scapularis")
        .institutionID(INSTITUTION_NAME)
        .build();
    return List.of(withoutImages, testDarwin());
  }

}
