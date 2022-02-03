package eu.dissco.demoapicollector.configuration;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MutliThreadingConfiguration {

  @Bean
  public Executor taskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setQueueCapacity(0);
    executor.setThreadNamePrefix("Cordra-");
    executor.setPrestartAllCoreThreads(true);
    executor.initialize();
    return executor;
  }

}
