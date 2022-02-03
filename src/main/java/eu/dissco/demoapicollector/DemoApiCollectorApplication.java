package eu.dissco.demoapicollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableAsync
@ConfigurationPropertiesScan
public class DemoApiCollectorApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApiCollectorApplication.class, args);
  }

}
