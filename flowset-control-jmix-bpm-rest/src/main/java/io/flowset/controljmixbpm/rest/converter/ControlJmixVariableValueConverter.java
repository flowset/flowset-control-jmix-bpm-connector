/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.converter;

import io.flowset.controljmixbpm.rest.dto.JmixEntityItemValue;
import io.flowset.controljmixbpm.rest.dto.response.VariableValueInfo;
import io.jmix.bpm.entity.FileDescription;
import io.jmix.core.MetadataTools;
import io.jmix.core.entity.EntityValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component("control_JmixVariableConverter")
public class ControlJmixVariableValueConverter {
    private static final Logger log = LoggerFactory.getLogger(ControlJmixVariableValueConverter.class);

    private final MetadataTools metadataTools;

    private final ObjectMapper objectMapper;

    public ControlJmixVariableValueConverter(MetadataTools metadataTools,
                                             ObjectMapper objectMapper) {
        this.metadataTools = metadataTools;
        this.objectMapper = objectMapper;
    }

    public VariableValueInfo createEntityVariableValueInfo(Object value) {
        VariableValueInfo variableValueInfo = new VariableValueInfo();
        variableValueInfo.setObjectTypeName(value != null ?
                value.getClass().getName() : null);

        variableValueInfo.setSerializationDataFormat(
                "application/json"
        );
        return variableValueInfo;
    }

    public VariableValueInfo createFileDescriptionVariableValueInfo(Object variableInstanceValue) {
        VariableValueInfo variableValueInfo = new VariableValueInfo();
        if (variableInstanceValue instanceof FileDescription fileDescription) {
            variableValueInfo.setFilename(fileDescription.getFileName());
        }
        return variableValueInfo;
    }

    public String serializeEntityList(List<?> list) {
        List<JmixEntityItemValue> result = list.stream()
                .map(entity -> {
                    JmixEntityItemValue value = new JmixEntityItemValue();
                    Object id = EntityValues.getId(entity);
                    value.setId(id != null ? id.toString() : null);
                    value.setInstanceName(metadataTools.getInstanceName(entity));

                    return value;
                })
                .collect(Collectors.toList());
        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(result);
        } catch (JacksonException e) {
            log.error("Error on entity list serialization", e);
            jsonValue = e.getMessage();
        }
        return jsonValue;
    }
}
