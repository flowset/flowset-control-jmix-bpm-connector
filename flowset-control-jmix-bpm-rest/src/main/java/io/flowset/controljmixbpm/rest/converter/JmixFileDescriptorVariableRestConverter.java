/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.converter;

import io.jmix.bpm.entity.FileDescription;
import org.flowable.common.rest.variable.EngineRestVariable;
import org.flowable.common.rest.variable.RestVariableConverter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// FileDescription is also a Jmix Entity, so it is accepted by JmixEntityVariableRestConverter too. This converter
// must have higher precedence than that general entity converter so a file variable is reported as "file-description"
// rather than "entity".
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@Component("fsc_JmixFileDescriptorVariableRestConverter")
public class JmixFileDescriptorVariableRestConverter implements RestVariableConverter {

    @Override
    public String getRestTypeName() {
        return "file-description";
    }

    @Override
    public Class<?> getVariableType() {
        return FileDescription.class; // parent class for Jmix entities
    }

    @Override
    public Object getVariableValue(EngineRestVariable result) {
        return null;
    }

    @Override
    public void convertVariableValue(Object variableValue, EngineRestVariable result) {

    }
}
