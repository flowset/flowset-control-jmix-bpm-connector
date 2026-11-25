/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.swagger.annotations.ApiParam;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.common.rest.api.RequestUtil;
import org.flowable.engine.ManagementService;
import org.flowable.job.api.DeadLetterJobQuery;
import org.flowable.rest.service.api.BpmnRestApiInterceptor;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.management.DeadLetterJobCollectionResource;
import org.flowable.rest.service.api.management.JobQueryProperties;
import org.flowable.rest.service.api.management.JobResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

/**
 * Exposes additional endpoints for Flowset Control:
 * <ol>
 *     <li>/control/management/deadletter-jobs with support of filtering by process definition key</li>
 * </ol>
 */
@RestController("fsc_DeadLetterJobRestController")
@RequestMapping("/control")
public class DeadLetterJobRestController {
    @Autowired
    protected RestResponseFactory restResponseFactory;

    @Autowired
    protected ManagementService managementService;

    @Autowired(required = false)
    protected BpmnRestApiInterceptor restApiInterceptor;

    @GetMapping(value = "/management/deadletter-jobs", produces = "application/json")
    public DataResponse<JobResponse> getJobs(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        DeadLetterJobQuery query = managementService.createDeadLetterJobQuery();

        // custom params
        if (allRequestParams.containsKey("processDefinitionKey")) {
            query.processDefinitionKey(allRequestParams.get("processDefinitionKey"));
        }

        // other params
        if (allRequestParams.containsKey("id")) {
            query.jobId(allRequestParams.get("id"));
        }
        if (allRequestParams.containsKey("processInstanceId")) {
            query.processInstanceId(allRequestParams.get("processInstanceId"));
        }
        if (allRequestParams.containsKey("withoutProcessInstanceId") && Boolean.parseBoolean(allRequestParams.get("withoutProcessInstanceId"))) {
            query.withoutProcessInstanceId();
        }
        if (allRequestParams.containsKey("executionId")) {
            query.executionId(allRequestParams.get("executionId"));
        }
        if (allRequestParams.containsKey("processDefinitionId")) {
            query.processDefinitionId(allRequestParams.get("processDefinitionId"));
        }
        if (allRequestParams.containsKey("elementId")) {
            query.elementId(allRequestParams.get("elementId"));
        }
        if (allRequestParams.containsKey("elementName")) {
            query.elementName(allRequestParams.get("elementName"));
        }
        if (allRequestParams.containsKey("handlerType")) {
            query.handlerType(allRequestParams.get("handlerType"));
        }
        if (allRequestParams.containsKey("handlerTypes")) {
            query.handlerTypes(Arrays.asList(allRequestParams.get("handlerTypes").split(",")));
        }
        if (allRequestParams.containsKey("executable")) {
            query.executable();
        }
        if (allRequestParams.containsKey("timersOnly")) {
            if (allRequestParams.containsKey("messagesOnly")) {
                throw new FlowableIllegalArgumentException("Only one of 'timersOnly' or 'messagesOnly' can be provided.");
            }
            if (Boolean.parseBoolean(allRequestParams.get("timersOnly"))) {
                query.timers();
            }
        }
        if (allRequestParams.containsKey("messagesOnly") && Boolean.parseBoolean(allRequestParams.get("messagesOnly"))) {
            query.messages();
        }
        if (allRequestParams.containsKey("dueBefore")) {
            query.duedateLowerThan(RequestUtil.getDate(allRequestParams, "dueBefore"));
        }
        if (allRequestParams.containsKey("dueAfter")) {
            query.duedateHigherThan(RequestUtil.getDate(allRequestParams, "dueAfter"));
        }
        if (allRequestParams.containsKey("withException") && Boolean.parseBoolean(allRequestParams.get("withException"))) {
            query.withException();
        }
        if (allRequestParams.containsKey("exceptionMessage")) {
            query.exceptionMessage(allRequestParams.get("exceptionMessage"));
        }
        if (allRequestParams.containsKey("tenantId")) {
            query.jobTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            query.jobTenantIdLike(allRequestParams.get("tenantIdLike"));
        }
        if (allRequestParams.containsKey("withoutTenantId") && Boolean.parseBoolean(allRequestParams.get("withoutTenantId"))) {
            query.jobWithoutTenantId();
        }
        if (allRequestParams.containsKey("withoutScopeType") && Boolean.parseBoolean(allRequestParams.get("withoutScopeType"))) {
            query.withoutScopeType();
        }
        if (allRequestParams.containsKey("withoutScopeId") && Boolean.parseBoolean(allRequestParams.get("withoutScopeId"))) {
            query.withoutScopeId();
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.accessDeadLetterJobInfoWithQuery(query);
        }

        return paginateList(allRequestParams, query, "id", JobQueryProperties.PROPERTIES, restResponseFactory::createDeadLetterJobResponseList);
    }
}
