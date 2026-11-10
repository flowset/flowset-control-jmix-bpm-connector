/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.flowset.controljmixbpm.rest.dto.request.ControlHistoricActivityInstanceQueryRequest;
import io.swagger.annotations.ApiParam;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.impl.HistoricActivityInstanceQueryProperty;
import org.flowable.rest.service.api.history.HistoricActivityInstanceBaseResource;
import org.flowable.rest.service.api.history.HistoricActivityInstanceResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

/**
 * Exposes endpoints for querying historic activity instances:
 * <ol>
 *     <li>/query/historic-activity-instances</li> - adds additional filtering options like "finishedBefore", "finishedAfter"
 * </ol>
 */
@RestController
@RequestMapping("/control")
public class HistoricActivityInstanceControlRestController extends HistoricActivityInstanceBaseResource {

    protected static final Map<String, QueryProperty> allowedSortProperties;

    static {
        Map<String, QueryProperty> sortProperties = new HashMap<>();
        sortProperties.put("activityId", HistoricActivityInstanceQueryProperty.ACTIVITY_ID);
        sortProperties.put("activityName", HistoricActivityInstanceQueryProperty.ACTIVITY_NAME);
        sortProperties.put("activityType", HistoricActivityInstanceQueryProperty.ACTIVITY_TYPE);
        sortProperties.put("duration", HistoricActivityInstanceQueryProperty.DURATION);
        sortProperties.put("endTime", HistoricActivityInstanceQueryProperty.END);
        sortProperties.put("executionId", HistoricActivityInstanceQueryProperty.EXECUTION_ID);
        sortProperties.put("activityInstanceId", HistoricActivityInstanceQueryProperty.HISTORIC_ACTIVITY_INSTANCE_ID);
        sortProperties.put("processDefinitionId", HistoricActivityInstanceQueryProperty.PROCESS_DEFINITION_ID);
        sortProperties.put("processInstanceId", HistoricActivityInstanceQueryProperty.PROCESS_INSTANCE_ID);
        sortProperties.put("startTime", HistoricActivityInstanceQueryProperty.START);
        sortProperties.put("tenantId", HistoricActivityInstanceQueryProperty.TENANT_ID);

        allowedSortProperties = Collections.unmodifiableMap(sortProperties);
    }

    @PostMapping(value = "/query/historic-activity-instances", produces = "application/json")
    public DataResponse<HistoricActivityInstanceResponse> queryActivityInstances(@RequestBody ControlHistoricActivityInstanceQueryRequest queryRequest, @ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {

        return getQueryResponse(queryRequest, allRequestParams);
    }

    protected DataResponse<HistoricActivityInstanceResponse> getQueryResponse(ControlHistoricActivityInstanceQueryRequest queryRequest, Map<String, String> allRequestParams) {
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();

        // Custom parameter handling
        if (queryRequest.getFinishedBefore() != null) {
            query.finishedBefore(queryRequest.getFinishedBefore());
        }

        if (queryRequest.getFinishedAfter() != null) {
            query.finishedAfter(queryRequest.getFinishedAfter());
        }

        // Populate query based on request
        if (queryRequest.getActivityId() != null) {
            query.activityId(queryRequest.getActivityId());
        }

        if (queryRequest.getActivityInstanceId() != null) {
            query.activityInstanceId(queryRequest.getActivityInstanceId());
        }

        if (queryRequest.getActivityName() != null) {
            query.activityName(queryRequest.getActivityName());
        }

        if (queryRequest.getActivityType() != null) {
            query.activityType(queryRequest.getActivityType());
        }

        if (queryRequest.getExecutionId() != null) {
            query.executionId(queryRequest.getExecutionId());
        }

        if (queryRequest.getFinished() != null) {
            Boolean finished = queryRequest.getFinished();
            if (finished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }

        if (queryRequest.getTaskAssignee() != null) {
            query.taskAssignee(queryRequest.getTaskAssignee());
        }

        if (queryRequest.getProcessInstanceId() != null) {
            query.processInstanceId(queryRequest.getProcessInstanceId());
        }

        if (queryRequest.getProcessInstanceIds() != null && !queryRequest.getProcessInstanceIds().isEmpty()) {
            query.processInstanceIds(queryRequest.getProcessInstanceIds());
        }

        if (queryRequest.getProcessDefinitionId() != null) {
            query.processDefinitionId(queryRequest.getProcessDefinitionId());
        }

        if (queryRequest.getCalledProcessInstanceIds() != null && !queryRequest.getCalledProcessInstanceIds().isEmpty()) {
            query.calledProcessInstanceIds(queryRequest.getCalledProcessInstanceIds());
        }

        if (queryRequest.getTenantId() != null) {
            query.activityTenantId(queryRequest.getTenantId());
        }

        if (queryRequest.getTenantIdLike() != null) {
            query.activityTenantIdLike(queryRequest.getTenantIdLike());
        }

        if (Boolean.TRUE.equals(queryRequest.getWithoutTenantId())) {
            query.activityWithoutTenantId();
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.accessHistoryActivityInfoWithQuery(query, queryRequest);
        }

        return paginateList(allRequestParams, queryRequest, query, "startTime", allowedSortProperties,
                restResponseFactory::createHistoricActivityInstanceResponseList);
    }
}

