package eu.dissco.demoapicollector;

import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.domain.DarwinCore;
import eu.dissco.demoapicollector.domain.Image;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static final String INSTITUTION_NAME = "Naturalis Biodiversity Center";
  public static final String ROR_INSTITUTION = "https://ror.org/0566bfb96";
  public static final String OCCURRENCE_ID = "https://data.biodiversitydata.nl/naturalis/specimen/ZMA.AVES.64848";
  public static final String ID = "ZMA.AVES.64848@CRS";
  public static final String CATALOG_NUMBER = "ZMA.AVES.64848";
  public static final String BASIS_OF_RECORD = "PreservedSpecimen";
  public static final String SCIENTIFIC_NAME = "sylvia hypola∩s";

  public static String loadResourceFile(String fileName) throws IOException {
    return new String(new ClassPathResource(fileName).getInputStream()
        .readAllBytes(), StandardCharsets.UTF_8);
  }

  public static DarwinCore testDarwin() {
    return DarwinCore.builder()
        .id(ID)
        .occurrenceID(OCCURRENCE_ID)
        .catalogNumber(CATALOG_NUMBER)
        .basisOfRecord(BASIS_OF_RECORD)
        .scientificName(SCIENTIFIC_NAME)
        .images(List.of(testImage()))
        .institutionID(INSTITUTION_NAME).build();
  }

  public static Authoritative testAuthoritative() {
    return Authoritative.builder()
        .midslevel(1)
        .curatedObjectID(OCCURRENCE_ID)
        .physicalSpecimenId(ID)
        .institutionCode(INSTITUTION_NAME)
        .institution(ROR_INSTITUTION)
        .materialType(BASIS_OF_RECORD)
        .name(SCIENTIFIC_NAME)
        .scientificName(SCIENTIFIC_NAME)
        .images(List.of(testImage()))
        .build();
  }

  public static Image testImage(){
    return Image.builder()
        .imageUrl("https://medialib.naturalis.nl/file/id/ZMA.AVES.64848/format/large")
        .format("image/jpeg")
        .variant("ac:GoodQuality")
        .build();
  }

}
