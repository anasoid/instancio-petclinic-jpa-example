package org.anasoid.instancio.petclinic.jpa.example.core.properties;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Integer dataSize;
    @NotNull
    private String csvPath;
    @NotNull
    Map<String, GeneratorProperties> entities;

    @Getter
    @Setter
    public static class GeneratorProperties {
        @NotNull
        private Integer minElement;
        @NotNull
        private Integer maxElement;
        @NotNull
        private Integer percentElement;

        private Boolean forceGenerateElement;

        public GeneratorProperties() {
        }
    }
}
