package io.flowset.controljmixbpm.rest.converter;

import io.flowset.controljmixbpm.rest.dto.response.*;
import org.flowable.common.rest.util.RestUrlBuilder;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricVariableUpdate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.Execution;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.history.HistoricDetailResponse;
import org.flowable.rest.service.api.history.HistoricProcessInstanceResponse;
import org.flowable.rest.service.api.history.HistoricVariableInstanceResponse;
import org.flowable.rest.service.api.runtime.VariableInstanceResponse;
import org.flowable.rest.service.api.runtime.process.ExecutionResponse;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Extends the default RestResponseFactory to add additional information about the variable value.
 * This class provides methods to create various types of responses for different entities and variables.
 */
public class ControlRestResponseFactory extends RestResponseFactory {

    public static final String JMIX_ENTITY_TYPE = "entity";
    public static final String JMIX_ENTITY_LIST_TYPE = "entity-list";
    public static final String JMIX_FILE_DESCRIPTION_TYPE = "file-description";
    public static final String JMIX_OUTCOMES_CONTAINER_TYPE = "outcomes-container";


    private final ControlJmixVariableValueConverter jmixVariableValueConverter;

    public ControlRestResponseFactory(ObjectMapper objectMapper,
                                      ControlJmixVariableValueConverter jmixVariableValueConverter) {
        super(objectMapper);
        this.jmixVariableValueConverter = jmixVariableValueConverter;
    }

    @Override
    public ExecutionResponse createExecutionResponse(Execution execution, RestUrlBuilder urlBuilder) {
        ExecutionResponse base = super.createExecutionResponse(execution, urlBuilder);
        ControlExecutionResponse result = new ControlExecutionResponse();
        BeanUtils.copyProperties(base, result);

        if (execution instanceof ExecutionEntity executionEntity) {
            result.setMultiInstanceRoot(executionEntity.isMultiInstanceRoot());
        }
        return result;
    }

    @Override
    public HistoricProcessInstanceResponse createHistoricProcessInstanceResponse(HistoricProcessInstance processInstance, RestUrlBuilder urlBuilder) {
        HistoricProcessInstanceResponse base = super.createHistoricProcessInstanceResponse(processInstance, urlBuilder);
        ControlHistoricProcessInstanceResponse result = new ControlHistoricProcessInstanceResponse();
        BeanUtils.copyProperties(base, result);

        result.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        return result;
    }

    @Override
    public HistoricVariableInstanceResponse createHistoricVariableInstanceResponse(HistoricVariableInstance variableInstance, RestUrlBuilder urlBuilder) {
        HistoricVariableInstanceResponse base = super.createHistoricVariableInstanceResponse(variableInstance, urlBuilder);
        ControlHistoricVariableInstanceResponse result = new ControlHistoricVariableInstanceResponse();
        BeanUtils.copyProperties(base, result);

        result.setTime(variableInstance.getTime());
        result.setCreateTime(variableInstance.getCreateTime());
        result.setLastUpdateTime(variableInstance.getLastUpdatedTime());
        fillJmixVariablesInfo(variableInstance.getVariableTypeName(), variableInstance.getValue(),
                result.getVariable(), result);
        return result;
    }

    @Override
    public HistoricDetailResponse createHistoricDetailResponse(HistoricDetail detail, RestUrlBuilder urlBuilder) {
        HistoricDetailResponse base = super.createHistoricDetailResponse(detail, urlBuilder);
        ControlHistoricDetailResponse result = new ControlHistoricDetailResponse();
        BeanUtils.copyProperties(base, result);

        if (detail instanceof HistoricVariableUpdate variableUpdate) {
            fillJmixVariablesInfo(variableUpdate.getVariableTypeName(), variableUpdate.getValue(),
                    result.getVariable(), result);
        }
        return result;
    }

    @Override
    public VariableInstanceResponse createVariableInstanceResponse(VariableInstance variableInstance, RestUrlBuilder urlBuilder) {
        VariableInstanceResponse base = super.createVariableInstanceResponse(variableInstance, urlBuilder);
        ControlVariableInstanceResponse result = new ControlVariableInstanceResponse();
        BeanUtils.copyProperties(base, result);

        fillJmixVariablesInfo(variableInstance.getTypeName(), variableInstance.getValue(),
                result.getVariable(), result);

        return result;
    }

    protected void fillJmixVariablesInfo(@Nullable String variableTypeName,
                                         @Nullable Object variableInstanceValue,
                                         @Nullable RestVariable variable,
                                         HasValueInfo result) {
        if (variableTypeName == null) {
            return;
        }
        if (variable != null) {
            variable.setType(variableTypeName);
        }

        switch (variableTypeName) {
            case JMIX_ENTITY_TYPE -> {
                VariableValueInfo variableValueInfo = jmixVariableValueConverter.createEntityVariableValueInfo(variableInstanceValue);
                result.setValueInfo(variableValueInfo);
            }
            case JMIX_FILE_DESCRIPTION_TYPE -> {
                VariableValueInfo variableValueInfo = jmixVariableValueConverter.createFileDescriptionVariableValueInfo(variableInstanceValue);
                result.setValueInfo(variableValueInfo);
            }
            case JMIX_ENTITY_LIST_TYPE -> {
                if (variable == null) {
                    return;
                }
                variable.setType(variableTypeName);
                variable.setValueUrl(null);

                if (variableInstanceValue != null) {
                    List<?> list = (List<?>) variableInstanceValue;
                    if (!list.isEmpty()) {
                        VariableValueInfo variableValueInfo = jmixVariableValueConverter.createEntityVariableValueInfo(list.getFirst());
                        result.setValueInfo(variableValueInfo);
                    }
                    variable.setValue(jmixVariableValueConverter.serializeEntityList(list));
                }
            }
        }
    }
}
