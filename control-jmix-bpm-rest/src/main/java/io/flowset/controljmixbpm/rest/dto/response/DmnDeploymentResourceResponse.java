/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.response;

/**
 * DTO for DMN deployment resource.
 */
public class DmnDeploymentResourceResponse {
    protected String id;
    protected String url;
    protected String dataUrl;
    protected String mediaType;
    protected String type;

    public DmnDeploymentResourceResponse(String resourceId, String resourceUrl,
                                         String resourceContentUrl, String contentType, String type) {
        this.id = resourceId;
        this.url = resourceUrl;
        this.dataUrl = resourceContentUrl;
        this.mediaType = contentType;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
