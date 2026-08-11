package io.flowset.autoconfigure.controljmixbpm.rest;

import io.flowset.controljmixbpm.rest.ControlJmixBpmRestConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({ControlJmixBpmRestConfiguration.class})
public class ControlJmixBpmRestAutoConfiguration {
}

