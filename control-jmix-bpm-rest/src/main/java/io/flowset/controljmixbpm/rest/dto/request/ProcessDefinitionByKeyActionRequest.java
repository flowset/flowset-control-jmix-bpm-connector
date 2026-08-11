/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.request;

import org.flowable.rest.service.api.RestActionRequest;

import java.util.Date;

/**
 * Request for activating or suspending a process definition by key.
 */
public class ProcessDefinitionByKeyActionRequest  extends RestActionRequest {
    public static final String ACTION_SUSPEND = "suspend";
    public static final String ACTION_ACTIVATE = "activate";

    private boolean includeProcessInstances;
    private Date date;
    protected String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isIncludeProcessInstances() {
        return includeProcessInstances;
    }

    public void setIncludeProcessInstances(boolean includeProcessInstances) {
        this.includeProcessInstances = includeProcessInstances;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
