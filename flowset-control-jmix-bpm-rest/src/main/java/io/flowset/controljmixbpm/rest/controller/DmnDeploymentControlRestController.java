/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import io.flowset.controljmixbpm.rest.converter.ControlDmnRestResponseFactory;
import io.flowset.controljmixbpm.rest.dto.response.DmnDeploymentResourceResponse;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.dmn.api.DmnDeployment;
import org.flowable.dmn.rest.service.api.DmnRestResponseFactory;
import org.flowable.dmn.rest.service.api.repository.DmnDeploymentResourceDataResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exposes endpoints:
 * <ol>
 *     <li><code>/control/dmn-repository/deployments/{deploymentId}/resourcedata</code>-
 *     returns resource data for a DMN deployment resource
 *     </li>
 *     <li><code>/control/dmn-repository/deployments/{id}/resources</code>- returns resource list for a DMN deployment</li>
 * </ol>
 */
@RestController
@RequestMapping("/control")
public class DmnDeploymentControlRestController extends DmnDeploymentResourceDataResource {

    @Autowired
    protected DmnRestResponseFactory restResponseFactory;

    @GetMapping(value = "/dmn-repository/deployments/{id}/resources", produces = "application/json")
    public List<DmnDeploymentResourceResponse> getDmnDeploymentsResource(@ApiParam(name = "id") @PathVariable String id) {
        DmnDeployment deployment = dmnRepositoryService.createDeploymentQuery().deploymentId(id).singleResult();
        if (deployment == null) {
            throw new FlowableObjectNotFoundException("Could not find a DMN deployment with id '" + id + "'.", DmnDeployment.class);
        } else {
            if (this.restApiInterceptor != null) {
                this.restApiInterceptor.accessDeploymentById(deployment);
            }

            List<String> resourceList = dmnRepositoryService.getDeploymentResourceNames(id);
            return getDmnDeploymentResourceResponseList(id, resourceList);
        }
    }


    @GetMapping("/dmn-repository/deployments/{deploymentId}/resourcedata")
    @ResponseBody
    public byte[] getDmnDeploymentResource(@PathVariable("deploymentId") String deploymentId, @RequestParam("resourceName") String resourceName, HttpServletResponse response) {
        return getDmnDeploymentResourceData(deploymentId, resourceName, response);
    }

    protected List<DmnDeploymentResourceResponse> getDmnDeploymentResourceResponseList(String id, List<String> resourceList) {
        return ((ControlDmnRestResponseFactory) this.restResponseFactory).createDmnDeploymentResourceResponseList(id,
                resourceList, this.contentTypeResolver);
    }

}
