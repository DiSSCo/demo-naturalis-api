package eu.dissco.demoapicollector.service;

import static eu.dissco.demoapicollector.TestUtils.testAuthoritative;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import eu.dissco.demoapicollector.properties.CordraPropteries;
import java.util.List;
import net.cnri.cordra.api.CordraClient;
import net.cnri.cordra.api.CordraException;
import net.cnri.cordra.api.CordraObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CordraServiceTest {

  @Mock
  private CordraClient client;

  @Mock
  private CordraPropteries properties;

  private CordraService service;

  @BeforeEach
  void setup() {
    this.service = new CordraService(client, properties);
  }

  @Test
  void testSaveItems() throws CordraException {
    // Given
    given(properties.getType()).willReturn("DigitalSpecimen");
    given(properties.getDryrun()).willReturn(true);
    given(client.create(any(CordraObject.class), anyBoolean())).willReturn(
        new CordraObject("DigitalSpecimen", "test"));

    // When
    service.saveItems(List.of(testAuthoritative()));

    // Then
    then(client).should().create(any(CordraObject.class), eq(true));
  }

}
