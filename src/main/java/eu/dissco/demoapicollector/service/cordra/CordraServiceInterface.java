package eu.dissco.demoapicollector.service.cordra;

import eu.dissco.demoapicollector.domain.Authoritative;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CordraServiceInterface {

  CompletableFuture<Boolean> saveItems(Authoritative item);

  CompletableFuture<Boolean> updateItems(Object object);

  List<Object> gatherObjects(int numberOfObjects);

  List<String> gatherIds(int numberOfObjects);

  CompletableFuture<Boolean> deleteItems(String digitalObject);
}
