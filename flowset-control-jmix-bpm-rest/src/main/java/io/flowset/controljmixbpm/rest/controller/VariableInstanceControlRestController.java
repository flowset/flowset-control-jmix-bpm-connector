/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.jmix.bpm.entity.FileDescription;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.HistoryService;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.history.HistoricVariableInstanceResponse;
import org.flowable.rest.service.api.runtime.VariableInstanceBaseResource;
import org.flowable.rest.service.api.runtime.VariableInstanceResponse;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Exposes endpoints for querying variable instances:
 * <ol>
 *     <li>/control/runtime/variable-instances/{varInstanceId}</li>
 *     <li>/control/runtime/variable-instances/{varInstanceId}/data</li>
 *     <li>/control/history/historic-variable-instances/{varInstanceId}</li>
 *     <li>/control/history/historic-variable-instances/{varInstanceId}/data</li>
 * </ol>
 */
@RestController
@RequestMapping("/control")
public class VariableInstanceControlRestController extends VariableInstanceBaseResource {

    @Autowired
    protected HistoryService historyService;

    @GetMapping(value = "/runtime/variable-instances/{varInstanceId}", produces = "application/json")
    public VariableInstanceResponse getVariableInstance(@ApiParam(name = "varInstanceId") @PathVariable String varInstanceId) {
        VariableInstance variableInstance = getVariableInstanceFromRequest(varInstanceId);
        VariableInstanceResponse variableInstanceResponse = restResponseFactory.createVariableInstanceResponse(variableInstance);

        return variableInstanceResponse;
    }

    @ResponseBody
    @GetMapping(value = "/runtime/variable-instances/{varInstanceId}/data")
    public byte[] getVariableInstanceData(@ApiParam(name = "varInstanceId") @PathVariable String varInstanceId,
                                          HttpServletResponse response) {
        VariableInstance variableInstance = getVariableInstanceFromRequest(varInstanceId);

        return getVariableDataByteArray(variableInstance, response);
    }


    @GetMapping(value = "/history/historic-variable-instances/{varInstanceId}", produces = "application/json")
    public HistoricVariableInstanceResponse getHistoricVariableInstance(@ApiParam(name = "varInstanceId") @PathVariable String varInstanceId) {
        HistoricVariableInstance variableInstance = getHistoricVariableInstanceFromRequest(varInstanceId);
        HistoricVariableInstanceResponse variableInstanceResponse = restResponseFactory.createHistoricVariableInstanceResponse(variableInstance);

        return variableInstanceResponse;
    }

    @ResponseBody
    @GetMapping(value = "/history/historic-variable-instances/{varInstanceId}/data")
    public byte[] getHistoricVariableInstanceData(@ApiParam(name = "varInstanceId") @PathVariable String varInstanceId,
                                                  HttpServletResponse response) {
        HistoricVariableInstance variableInstance = getHistoricVariableInstanceFromRequest(varInstanceId);

        return getVariableDataByteArray(variableInstance, response);
    }

    protected VariableInstance getVariableInstanceFromRequest(String variableInstanceId) {
        VariableInstance variableInstance = getVariableInstanceFromRequestWithoutAccessCheck(variableInstanceId);

        if (restApiInterceptor != null) {
            restApiInterceptor.accessVariableInfoById(variableInstance);
        }

        return variableInstance;
    }

    protected HistoricVariableInstance getHistoricVariableInstanceFromRequest(String processInstanceId) {
        HistoricVariableInstance variableInstance = getHistoricVariableInstanceFromRequestWithoutAccessCheck(processInstanceId);

        if (restApiInterceptor != null) {
            restApiInterceptor.accessHistoryVariableInfoById(variableInstance);
        }

        return variableInstance;
    }

    protected byte[] getVariableDataByteArray(VariableInstance variableInstance,
                                              HttpServletResponse response) {
        return getVariableDataByteArray(variableInstance.getValue(), variableInstance.getName(),
                variableInstance.getId(), RestResponseFactory.VARIABLE_VARINSTANCE, response);
    }

    protected byte[] getVariableDataByteArray(HistoricVariableInstance variableInstance,
                                              HttpServletResponse response) {
        return getVariableDataByteArray(variableInstance.getValue(), variableInstance.getVariableName(),
                variableInstance.getId(), RestResponseFactory.VARIABLE_HISTORY_VARINSTANCE, response);
    }

    protected byte[] getVariableDataByteArray(Object variableValue,
                                              String variableName,
                                              String variableInstanceId,
                                              int variableType,
                                              HttpServletResponse response) {

        try {
            byte[] result;

            // A Jmix file variable stores the file content as a raw byte array together with the file name,
            // so its content can be streamed directly.
            if (variableValue instanceof FileDescription fileDescription) {
                result = fileDescription.getFileContent();
                if (result == null) {
                    throw new FlowableObjectNotFoundException("The variable does not have a binary data stream.", null);
                }
                response.setContentType("application/octet-stream");
                String fileName = fileDescription.getFileName();
                if (fileName != null) {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                }
                return result;
            }

            RestVariable variable = restResponseFactory.createRestVariable(variableName, variableValue,
                    null, variableInstanceId, variableType, true);
            if (RestResponseFactory.BYTE_ARRAY_VARIABLE_TYPE.equals(variable.getType())) {
                result = (byte[]) variable.getValue();
                response.setContentType("application/octet-stream");

            } else if (RestResponseFactory.SERIALIZABLE_VARIABLE_TYPE.equals(variable.getType())) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (ObjectOutputStream outputStream = new ObjectOutputStream(buffer)) {
                    outputStream.writeObject(variable.getValue());
                }
                result = buffer.toByteArray();
                response.setContentType("application/x-java-serialized-object");

            } else {
                throw new FlowableObjectNotFoundException("The variable does not have a binary data stream.", null);
            }
            return result;

        } catch (IOException ioe) {
            throw new FlowableException("Error getting variable " + variableName, ioe);
        }
    }

    protected VariableInstance getVariableInstanceFromRequestWithoutAccessCheck(String variableInstanceId) {
        VariableInstance variableInstance = runtimeService.createVariableInstanceQuery()
                .id(variableInstanceId)
                .singleResult();
        if (variableInstance == null) {
            throw new FlowableObjectNotFoundException("Could not find a variable instance with id '" + variableInstanceId + "'.");
        }
        return variableInstance;
    }

    protected HistoricVariableInstance getHistoricVariableInstanceFromRequestWithoutAccessCheck(String variableInstanceId) {
        HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery()
                .id(variableInstanceId)
                .singleResult();
        if (variableInstance == null) {
            throw new FlowableObjectNotFoundException("Could not find a variable instance with id '" + variableInstanceId + "'.");
        }
        return variableInstance;
    }
}

