package io.flowset.controljmixbpm.autoconfigure.ui;

import io.flowset.controljmixbpm.ui.ControlJmixBpmUiConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({ControlJmixBpmUiConfiguration.class})
public class ControlJmixBpmUiAutoConfiguration {
}

