package eu.dissco.demoapicollector.domain;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DarwinCore {

  String id;
  String occurrenceID;
  String catalogNumber;
  String basisOfRecord;
  String scientificName;
  String institutionID;
  List<Image> images;
}
