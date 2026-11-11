/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.swagger.annotations.ApiParam;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.dmn.api.DmnDecisionQuery;
import org.flowable.dmn.engine.impl.DecisionQueryProperty;
import org.flowable.dmn.rest.service.api.repository.DecisionCollectionResource;
import org.flowable.dmn.rest.service.api.repository.DecisionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

/**
 * Exposes additional endpoints for decisions:
 * <ol>
 *     <li><code>/dmn-repository/decisions</code> with support for filtering "idIn"</li>
 * </ol>
 */
@RestController("fsc_DecisionControlRestController")
@RequestMapping("/control")
public class DecisionControlRestController extends DecisionCollectionResource {

    private static final Map<String, QueryProperty> properties = new HashMap<>();

    // copied from Flowable
    static {
        properties.put("id", DecisionQueryProperty.DECISION_ID);
        properties.put("key", DecisionQueryProperty.DECISION_KEY);
        properties.put("category", DecisionQueryProperty.DECISION_CATEGORY);
        properties.put("name", DecisionQueryProperty.DECISION_NAME);
        properties.put("version", DecisionQueryProperty.DECISION_VERSION);
        properties.put("deploymentId", DecisionQueryProperty.DECISION_DEPLOYMENT_ID);
        properties.put("tenantId", DecisionQueryProperty.DECISION_TENANT_ID);
        properties.put("decisionType", DecisionQueryProperty.DECISION_TYPE);
    }

    @Override
    @GetMapping(value = "/dmn-repository/decisions", produces = "application/json")
    public DataResponse<DecisionResponse> getDecisions(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        DmnDecisionQuery decisionQuery = dmnRepositoryService.createDecisionQuery();

        // Populate filter-parameters
        if (allRequestParams.containsKey("category")) {
            decisionQuery.decisionCategory(allRequestParams.get("category"));
        }
        if (allRequestParams.containsKey("categoryLike")) {
            decisionQuery.decisionCategoryLike(allRequestParams.get("categoryLike"));
        }
        if (allRequestParams.containsKey("categoryNotEquals")) {
            decisionQuery.decisionCategoryNotEquals(allRequestParams.get("categoryNotEquals"));
        }
        if (allRequestParams.containsKey("key")) {
            decisionQuery.decisionKey(allRequestParams.get("key"));
        }
        if (allRequestParams.containsKey("keyLike")) {
            decisionQuery.decisionKeyLike(allRequestParams.get("keyLike"));
        }
        if (allRequestParams.containsKey("name")) {
            decisionQuery.decisionName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("nameLike")) {
            decisionQuery.decisionNameLike(allRequestParams.get("nameLike"));
        }
        if (allRequestParams.containsKey("resourceName")) {
            decisionQuery.decisionResourceName(allRequestParams.get("resourceName"));
        }
        if (allRequestParams.containsKey("resourceNameLike")) {
            decisionQuery.decisionResourceNameLike(allRequestParams.get("resourceNameLike"));
        }
        if (allRequestParams.containsKey("version")) {
            decisionQuery.decisionVersion(Integer.valueOf(allRequestParams.get("version")));
        }
        if (allRequestParams.containsKey("latest")) {
            if (Boolean.parseBoolean(allRequestParams.get("latest"))) {
                decisionQuery.latestVersion();
            }
        }
        if (allRequestParams.containsKey("deploymentId")) {
            decisionQuery.deploymentId(allRequestParams.get("deploymentId"));
        }
        if (allRequestParams.containsKey("parentDeploymentId")) {
            decisionQuery.parentDeploymentId(allRequestParams.get("parentDeploymentId"));
        }
        if (allRequestParams.containsKey("tenantId")) {
            decisionQuery.decisionTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            decisionQuery.decisionTenantIdLike(allRequestParams.get("tenantIdLike"));
        }
        if (allRequestParams.containsKey("decisionType")) {
            decisionQuery.decisionType(allRequestParams.get("decisionType"));
        }
        if (allRequestParams.containsKey("decisionTypeLike")) {
            decisionQuery.decisionTypeLike(allRequestParams.get("decisionTypeLike"));
        }

        processControlParams(allRequestParams, decisionQuery);

        if (restApiInterceptor != null) {
            restApiInterceptor.accessDecisionTableInfoWithQuery(decisionQuery);
        }

        return paginateList(allRequestParams, decisionQuery, "name", properties,
                dmnRestResponseFactory::createDecisionResponseList);
    }

    protected void processControlParams(Map<String, String> allRequestParams, DmnDecisionQuery decisionQuery) {
        if (allRequestParams.containsKey("idIn")) {
            decisionQuery.decisionIds(Arrays.stream(allRequestParams.get("idIn")
                            .split(",\\s*"))
                    .collect(Collectors.toSet()));
        }
    }
}
