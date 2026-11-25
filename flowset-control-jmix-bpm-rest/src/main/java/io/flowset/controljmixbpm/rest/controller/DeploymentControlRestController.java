/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.flowable.rest.service.api.repository.DeploymentResourceDataResource;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes endpoints:
 * <ol>
 *     <li><code>/control/repository/deployments/{deploymentId}/resourcedata</code>- returns resource data for a BPMN deployment</li>
 * </ol>
 */
@RestController("fsc_DeploymentControlRestController")
@RequestMapping("/control")
public class DeploymentControlRestController extends DeploymentResourceDataResource {

    @GetMapping("/repository/deployments/{deploymentId}/resourcedata")
    @ResponseBody
    public byte[] getDeploymentResource(@PathVariable("deploymentId") String deploymentId,
                                           @RequestParam("resourceName") String resourceName, HttpServletResponse response) {
        return getDeploymentResourceData(deploymentId, resourceName, response);
    }

}
