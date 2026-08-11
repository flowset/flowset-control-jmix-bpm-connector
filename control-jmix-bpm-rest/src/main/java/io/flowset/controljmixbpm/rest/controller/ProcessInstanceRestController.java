/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.flowset.controljmixbpm.rest.dto.request.ControlExecutionChangeActivityStateRequest;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.engine.runtime.ChangeActivityStateBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.BaseProcessInstanceResource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exposes endpoints for querying process instances:
 * <ul>
 *     <li>/control/runtime/process-instances/{processInstanceId}/change-state with supporting for process variables</li>
 * </ul>
 */
@RestController
@RequestMapping("/control")
public class ProcessInstanceRestController extends BaseProcessInstanceResource {

    @PostMapping(value = "/runtime/process-instances/{processInstanceId}/change-state", produces = "application/json")
    public void changeProcessInstanceActivityState(@PathVariable String processInstanceId,
                                    @RequestBody ControlExecutionChangeActivityStateRequest activityStateRequest) {

        ProcessInstance processInstance = getProcessInstanceFromRequestWithoutAccessCheck(processInstanceId);

        if (restApiInterceptor != null) {
            restApiInterceptor.changeActivityState(processInstance, activityStateRequest);
        }

        Map<String, Object> globalVariables = new HashMap<>();
        Map<String, Object> localVariables = new HashMap<>();
        collectVariablesByScope(activityStateRequest.getVariables(), globalVariables, localVariables);

        ChangeActivityStateBuilder builder = null;
        if (activityStateRequest.getCancelActivityIds() != null && activityStateRequest.getCancelActivityIds().size() == 1) {
            builder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId)
                    .moveSingleActivityIdToActivityIds(activityStateRequest.getCancelActivityIds().get(0), activityStateRequest.getStartActivityIds());

        } else if (activityStateRequest.getStartActivityIds() != null && activityStateRequest.getStartActivityIds().size() == 1) {
            builder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId)
                    .moveActivityIdsToSingleActivityId(activityStateRequest.getCancelActivityIds(), activityStateRequest.getStartActivityIds().get(0));
        }

        if (builder != null) {
            builder.processVariables(globalVariables);
            if (!localVariables.isEmpty() && activityStateRequest.getStartActivityIds() != null) {
                for (String startActivityId : activityStateRequest.getStartActivityIds()) {
                    builder.localVariables(startActivityId, localVariables);
                }
            }
            builder.changeState();
        }

    }

    protected void collectVariablesByScope(List<RestVariable> restVariables,
                                           Map<String, Object> globalVariables,
                                           Map<String, Object> localVariables) {
        if (restVariables == null) {
            return;
        }
        for (RestVariable var : restVariables) {
            if (var.getName() == null) {
                throw new FlowableIllegalArgumentException("Variable name is required");
            }
            Object value = restResponseFactory.getVariableValue(var);
            if (var.getVariableScope() == RestVariable.RestVariableScope.LOCAL) {
                localVariables.put(var.getName(), value);
            } else {
                globalVariables.put(var.getName(), value);
            }
        }
    }
}
