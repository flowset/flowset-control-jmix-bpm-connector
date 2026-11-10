/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.flowset.controljmixbpm.rest.dto.request.ProcessDefinitionByKeyActionRequest;
import io.swagger.annotations.ApiParam;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.common.rest.exception.FlowableConflictException;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.rest.service.api.repository.ProcessDefinitionActionRequest;
import org.flowable.rest.service.api.repository.ProcessDefinitionCollectionResource;
import org.flowable.rest.service.api.repository.ProcessDefinitionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

/**
 * Exposes additional endpoints for process definitions:
 * <ol>
 *     <li><code>/repository/process-definitions</code> with support for filtering "idIn"</li>
 *     <li><code>/repository/process-definitions/key/{processDefinitionKey}</code> to support activate/suspend by key</li>
 * </ol>
 */
@RestController
@RequestMapping("/control")
public class ProcessDefinitionControlRestController extends ProcessDefinitionCollectionResource {

    private static final Map<String, QueryProperty> properties = new HashMap<>();

    // copied from Flowable
    static {
        properties.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
        properties.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
        properties.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
        properties.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
        properties.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
        properties.put("deploymentId", ProcessDefinitionQueryProperty.DEPLOYMENT_ID);
        properties.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
    }


    @GetMapping(value = "/repository/process-definitions", produces = "application/json")
    public DataResponse<ProcessDefinitionResponse> getProcessDefinitions(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        // Populate filter-parameters
        if (allRequestParams.containsKey("category")) {
            processDefinitionQuery.processDefinitionCategory(allRequestParams.get("category"));
        }
        if (allRequestParams.containsKey("categoryLike")) {
            processDefinitionQuery.processDefinitionCategoryLike(allRequestParams.get("categoryLike"));
        }
        if (allRequestParams.containsKey("categoryNotEquals")) {
            processDefinitionQuery.processDefinitionCategoryNotEquals(allRequestParams.get("categoryNotEquals"));
        }

        if (allRequestParams.containsKey("key")) {
            processDefinitionQuery.processDefinitionKey(allRequestParams.get("key"));
        }
        if (allRequestParams.containsKey("keyLike")) {
            processDefinitionQuery.processDefinitionKeyLike(allRequestParams.get("keyLike"));
        }
        if (allRequestParams.containsKey("name")) {
            processDefinitionQuery.processDefinitionName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("nameLike")) {
            processDefinitionQuery.processDefinitionNameLike(allRequestParams.get("nameLike"));
        }
        if (allRequestParams.containsKey("nameLikeIgnoreCase")) {
            processDefinitionQuery.processDefinitionNameLikeIgnoreCase(allRequestParams.get("nameLikeIgnoreCase"));
        }
        if (allRequestParams.containsKey("resourceName")) {
            processDefinitionQuery.processDefinitionResourceName(allRequestParams.get("resourceName"));
        }
        if (allRequestParams.containsKey("resourceNameLike")) {
            processDefinitionQuery.processDefinitionResourceNameLike(allRequestParams.get("resourceNameLike"));
        }
        if (allRequestParams.containsKey("version")) {
            processDefinitionQuery.processDefinitionVersion(Integer.valueOf(allRequestParams.get("version")));
        }
        if (allRequestParams.containsKey("suspended")) {
            if (Boolean.parseBoolean(allRequestParams.get("suspended"))) {
                processDefinitionQuery.suspended();
            } else {
                processDefinitionQuery.active();
            }
        }
        if (allRequestParams.containsKey("latest")) {
            if (Boolean.parseBoolean(allRequestParams.get("latest"))) {
                processDefinitionQuery.latestVersion();
            }
        }
        if (allRequestParams.containsKey("deploymentId")) {
            processDefinitionQuery.deploymentId(allRequestParams.get("deploymentId"));
        }
        if (allRequestParams.containsKey("parentDeploymentId")) {
            processDefinitionQuery.parentDeploymentId(allRequestParams.get("parentDeploymentId"));
        }
        if (allRequestParams.containsKey("startableByUser")) {
            processDefinitionQuery.startableByUser(allRequestParams.get("startableByUser"));
        }
        if (allRequestParams.containsKey("tenantId")) {
            processDefinitionQuery.processDefinitionTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            processDefinitionQuery.processDefinitionTenantIdLike(allRequestParams.get("tenantIdLike"));
        }

        processControlParams(allRequestParams, processDefinitionQuery);

        if (restApiInterceptor != null) {
            restApiInterceptor.accessProcessDefinitionsWithQuery(processDefinitionQuery);
        }

        return paginateList(allRequestParams, processDefinitionQuery, "name", properties,
                restResponseFactory::createProcessDefinitionResponseList);
    }

    @PutMapping(value = "/repository/process-definitions/key/{processDefinitionKey}", produces = "application/json")
    public void executeProcessDefinitionAction(
            @ApiParam(name = "processDefinitionKey") @PathVariable String processDefinitionKey,
            @ApiParam(required = true) @RequestBody ProcessDefinitionByKeyActionRequest actionRequest) {

        if (actionRequest == null) {
            throw new FlowableIllegalArgumentException("No action found in request body.");
        }

        if (actionRequest.getAction() != null) {
            if (ProcessDefinitionActionRequest.ACTION_SUSPEND.equals(actionRequest.getAction())) {
                suspendProcessDefinition(processDefinitionKey,
                        actionRequest.isIncludeProcessInstances(), actionRequest.getDate(),
                        actionRequest.getTenantId());
                return;

            } else if (ProcessDefinitionActionRequest.ACTION_ACTIVATE.equals(actionRequest.getAction())) {
                activateProcessDefinitionByKey(processDefinitionKey, actionRequest.isIncludeProcessInstances(),
                        actionRequest.getDate(), actionRequest.getTenantId());
                return;
            }
        }

        throw new FlowableIllegalArgumentException("Invalid action: '" + actionRequest.getAction() + "'.");
    }

    protected void activateProcessDefinitionByKey(String processDefinitionKey, boolean suspendInstances,
                                                  Date date, String tenantId) {
        if (tenantId != null && !tenantId.isEmpty()) {
            repositoryService.activateProcessDefinitionByKey(processDefinitionKey, suspendInstances, date, tenantId);
        } else {
            repositoryService.activateProcessDefinitionByKey(processDefinitionKey, suspendInstances, date);
        }
    }

    protected void suspendProcessDefinition(String processDefinitionKey, boolean suspendInstances,
                                            Date date, String tenantId) {
        if (tenantId != null && !tenantId.isEmpty()) {
            repositoryService.suspendProcessDefinitionByKey(processDefinitionKey, suspendInstances, date, tenantId);
        } else {
            repositoryService.suspendProcessDefinitionByKey(processDefinitionKey, suspendInstances, date);
        }

    }

    protected void processControlParams(Map<String, String> allRequestParams, ProcessDefinitionQuery processDefinitionQuery) {
        // TODO: custom
        if (allRequestParams.containsKey("idIn")) {
            processDefinitionQuery.processDefinitionIds(Arrays.stream(allRequestParams.get("idIn")
                            .split(",\\s*"))
                    .collect(Collectors.toSet()));
        }
    }
}

