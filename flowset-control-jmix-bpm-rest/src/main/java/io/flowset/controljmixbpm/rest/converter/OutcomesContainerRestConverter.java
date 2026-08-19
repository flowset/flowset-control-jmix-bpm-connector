/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.converter;

import io.flowset.controljmixbpm.rest.dto.JmixEntityItemValue;
import io.jmix.bpm.data.outcome.OutcomesContainer;
import org.flowable.common.rest.variable.EngineRestVariable;
import org.flowable.common.rest.variable.RestVariableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component("control_OutcomesContainerRestConverter")
public class OutcomesContainerRestConverter implements RestVariableConverter {
    private static final Logger log = LoggerFactory.getLogger(JmixEntityItemValue.class);
    
    protected final ObjectMapper objectMapper;

    public OutcomesContainerRestConverter() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public String getRestTypeName() {
        return "outcomes-container";
    }

    @Override
    public Class<?> getVariableType() {
        return OutcomesContainer.class;
    }

    @Override
    public Object getVariableValue(EngineRestVariable result) {
        return null;
    }

    @Override
    public void convertVariableValue(Object variableValue, EngineRestVariable result) {
        if (variableValue != null) {
            try {
                String jsonValue = objectMapper.writeValueAsString(variableValue);
                result.setValue(jsonValue);
            } catch (JacksonException e) {
                log.error("Error on entity serialization", e);
                result.setValue(e.getMessage());
            }
        }
    }
}
