package eu.dissco.demoapicollector.domain;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Authoritative {

  int midslevel;
  String curatedObjectID;
  String institution;
  String institutionCode;
  String materialType;
  String name;
  String scientificName;
  String physicalSpecimenId;
  List<Image> images;
}
