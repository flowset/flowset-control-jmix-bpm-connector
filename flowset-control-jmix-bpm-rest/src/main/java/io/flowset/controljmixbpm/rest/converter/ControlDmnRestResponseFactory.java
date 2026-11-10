/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.converter;

import io.flowset.controljmixbpm.rest.dto.response.DmnDeploymentResourceResponse;
import org.flowable.common.rest.resolver.ContentTypeResolver;
import org.flowable.common.rest.variable.RestVariableConverter;
import org.flowable.dmn.rest.service.api.DmnRestResponseFactory;
import org.flowable.dmn.rest.service.api.DmnRestUrlBuilder;
import org.flowable.dmn.rest.service.api.DmnRestUrls;

import java.util.ArrayList;
import java.util.List;

import static org.flowable.dmn.engine.impl.deployer.DmnResourceUtil.DMN_RESOURCE_SUFFIXES;

/**
 * Factory class for creating DMN-specific REST responses.
 * This class extends the functionality of the {@link DmnRestResponseFactory} to provide
 * additional methods for constructing {@link DmnDeploymentResourceResponse} objects.
 */
public class ControlDmnRestResponseFactory extends DmnRestResponseFactory {

    /**
     * Creates a list of {@link DmnDeploymentResourceResponse} objects based on the provided deployment ID and resource list.
     *
     * @param deploymentId        DMN deployment identifier
     * @param resourceList        list of resource names
     * @param contentTypeResolver a resolver for determining the content type of resource
     * @return a list of {@link DmnDeploymentResourceResponse} instances
     */
    public List<DmnDeploymentResourceResponse> createDmnDeploymentResourceResponseList(String deploymentId, List<String> resourceList, ContentTypeResolver contentTypeResolver) {
        DmnRestUrlBuilder urlBuilder = createUrlBuilder();

        // Add additional metadata to the artifact-strings before returning
        List<DmnDeploymentResourceResponse> responseList = new ArrayList<>(resourceList.size());
        for (String resourceId : resourceList) {
            responseList.add(createDmnDeploymentResourceResponse(deploymentId, resourceId, contentTypeResolver.resolveContentType(resourceId), urlBuilder));
        }
        return responseList;
    }

    /**
     * Creates a {@link DmnDeploymentResourceResponse} object based on the provided deployment ID, resource ID, content type, and URL builder.
     *
     * @param deploymentId DMN deployment identifier
     * @param resourceId   DMN resource identifier
     * @param contentType  content type of the resource
     * @param urlBuilder   URL builder for constructing resource URLs
     * @return a {@link DmnDeploymentResourceResponse} instance
     */
    public DmnDeploymentResourceResponse createDmnDeploymentResourceResponse(String deploymentId, String resourceId, String contentType, DmnRestUrlBuilder urlBuilder) {
        // Create URL's
        String resourceContentUrl = urlBuilder.buildUrl(DmnRestUrls.URL_DEPLOYMENT_RESOURCE_CONTENT, deploymentId, resourceId);

        // Determine type
        String type = "resource";
        for (String suffix : DMN_RESOURCE_SUFFIXES) {
            if (resourceId.endsWith(suffix)) {
                type = "dmn";
                break;
            }
        }
        return new DmnDeploymentResourceResponse(resourceId, null, resourceContentUrl, contentType, type);
    }

    public List<RestVariableConverter> getVariableConverters() {
        return variableConverters;
    }
}
