/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.converter;

import io.flowset.controljmixbpm.rest.dto.JmixEntityItemValue;
import io.jmix.core.Entity;
import io.jmix.core.MetadataTools;
import io.jmix.core.entity.EntityValues;
import org.flowable.common.rest.variable.EngineRestVariable;
import org.flowable.common.rest.variable.RestVariableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Order
@Component("control_JmixEntityVariableRestConverter")
public class JmixEntityVariableRestConverter implements RestVariableConverter {
    private static final Logger log = LoggerFactory.getLogger(JmixEntityVariableRestConverter.class);

    private final ObjectMapper objectMapper;
    private final MetadataTools metadataTools;

    public JmixEntityVariableRestConverter(MetadataTools metadataTools) {
        this.objectMapper = new ObjectMapper();
        this.metadataTools = metadataTools;
    }

    @Override
    public String getRestTypeName() {
        return "entity";
    }

    @Override
    public Class<?> getVariableType() {
        return Entity.class; // parent class for Jmix entities
    }

    @Override
    public Object getVariableValue(EngineRestVariable result) {
        return null;
    }

    @Override
    public void convertVariableValue(Object variableValue, EngineRestVariable result) {
        if (variableValue != null) {

            try {
                JmixEntityItemValue value = new JmixEntityItemValue();
                Object id = EntityValues.getId(variableValue);
                value.setId(id != null ? id.toString() : null);
                value.setInstanceName(metadataTools.getInstanceName(variableValue));
                String jsonValue = objectMapper.writeValueAsString(value);
                result.setValue(jsonValue);
            } catch (JacksonException e) {
                log.error("Error on entity serialization", e);
                result.setValue(e.getMessage());
            }
        }
    }
}
