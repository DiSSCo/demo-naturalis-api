package eu.dissco.demoapicollector.service;

import eu.dissco.demoapicollector.domain.Authoritative;
import eu.dissco.demoapicollector.domain.DarwinCore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OpenDSMappingService {

  private final RORService rorService;

  public List<Authoritative> mapDarwinToOpenDS(List<DarwinCore> darwinRecords) {
    log.info("Mapping elements of DarwinCore to OpenDS");
    return darwinRecords.stream().map(this::mapToAuthoritative).toList();
  }

  private Authoritative mapToAuthoritative(DarwinCore darwinCore) {
    return Authoritative.builder()
        .curatedObjectID(darwinCore.getOccurrenceID())
        .name(darwinCore.getScientificName())
        .midslevel(1)
        .institution(rorService.getRoRId(darwinCore.getInstitutionID()))
        .materialType(darwinCore.getBasisOfRecord())
        .scientificName(darwinCore.getScientificName())
        .physicalSpecimenId(darwinCore.getId())
        .images(darwinCore.getImages())
        .institutionCode(darwinCore.getInstitutionID()).build();
  }


}
