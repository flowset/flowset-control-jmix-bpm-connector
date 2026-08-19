/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.flowset.controljmixbpm.rest.dto.request.ControlHistoricActivityInstanceQueryRequest;
import io.swagger.annotations.ApiParam;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.impl.HistoricActivityInstanceQueryProperty;
import org.flowable.rest.service.api.BpmnRestApiInterceptor;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.history.HistoricActivityInstanceBaseResource;
import org.flowable.rest.service.api.history.HistoricActivityInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    protected static Map<String, QueryProperty> allowedSortProperties = new HashMap<>();

    static {
        allowedSortProperties.put("activityId", HistoricActivityInstanceQueryProperty.ACTIVITY_ID);
        allowedSortProperties.put("activityName", HistoricActivityInstanceQueryProperty.ACTIVITY_NAME);
        allowedSortProperties.put("activityType", HistoricActivityInstanceQueryProperty.ACTIVITY_TYPE);
        allowedSortProperties.put("duration", HistoricActivityInstanceQueryProperty.DURATION);
        allowedSortProperties.put("endTime", HistoricActivityInstanceQueryProperty.END);
        allowedSortProperties.put("executionId", HistoricActivityInstanceQueryProperty.EXECUTION_ID);
        allowedSortProperties.put("activityInstanceId", HistoricActivityInstanceQueryProperty.HISTORIC_ACTIVITY_INSTANCE_ID);
        allowedSortProperties.put("processDefinitionId", HistoricActivityInstanceQueryProperty.PROCESS_DEFINITION_ID);
        allowedSortProperties.put("processInstanceId", HistoricActivityInstanceQueryProperty.PROCESS_INSTANCE_ID);
        allowedSortProperties.put("startTime", HistoricActivityInstanceQueryProperty.START);
        allowedSortProperties.put("tenantId", HistoricActivityInstanceQueryProperty.TENANT_ID);
    }

    @Autowired(required = false)
    protected BpmnRestApiInterceptor restApiInterceptor;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected RestResponseFactory restResponseFactory;

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

