/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.autoconfigure.controljmixbpm.flowui;

import io.flowset.controljmixbpm.flowui.ControlJmixBpmFlowuiConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({ControlJmixBpmFlowuiConfiguration.class})
public class ControlJmixBpmFlowuiAutoConfiguration {
}

