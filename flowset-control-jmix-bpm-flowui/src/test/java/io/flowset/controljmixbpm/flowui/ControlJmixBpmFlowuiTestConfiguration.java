/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui;

import io.jmix.core.annotation.JmixModule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(ControlJmixBpmFlowuiConfiguration.class)
@PropertySource("classpath:/io/flowset/controljmixbpm/flowui/test-app.properties")
@JmixModule(id = "io.flowset.controljmixbpm.flowui.test", dependsOn = ControlJmixBpmFlowuiConfiguration.class)
public class ControlJmixBpmFlowuiTestConfiguration {


}
