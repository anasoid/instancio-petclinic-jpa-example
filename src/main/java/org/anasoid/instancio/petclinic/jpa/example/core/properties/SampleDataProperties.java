package org.anasoid.instancio.petclinic.jpa.example.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "instancio.sample.data.config")
public class SampleDataProperties {

    private Integer dataSize;
    private String csvPath;

    Map<String, GeneratorProperties> entities;

    @Getter
    @Setter
    public static class GeneratorProperties {

        private Integer minElement;
        private Integer maxElement;
        private Integer percentElement;
        private Boolean forceGenerateElement;

        public GeneratorProperties() {
        }
    }
}
