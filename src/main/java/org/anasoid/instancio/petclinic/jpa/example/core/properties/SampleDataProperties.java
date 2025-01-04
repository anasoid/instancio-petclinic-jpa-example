package org.anasoid.instancio.petclinic.jpa.example.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "instantio.sample.data.config")
public class SampleDataProperties {

  private int dataSize;
}
