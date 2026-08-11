package io.flowset.controljmixbpm.ui;

import io.jmix.core.annotation.JmixModule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(ControlJmixBpmUiConfiguration.class)
@PropertySource("classpath:/io/flowset/controljmixbpm/ui/test-app.properties")
@JmixModule(id = "io.flowset.controljmixbpm.ui.test", dependsOn = ControlJmixBpmUiConfiguration.class)
public class ControlJmixBpmUiTestConfiguration {


}
