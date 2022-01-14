package eu.dissco.demoapicollector.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Image {

  String imageUrl;
  String format;
  String variant;
}
