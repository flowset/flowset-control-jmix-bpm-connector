/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.response;

import org.flowable.rest.service.api.history.HistoricProcessInstanceResponse;

/**
 * Extends Flowable's {@link HistoricProcessInstanceResponse} and includes additional fields:
 * <ol>
 *     <li><code>processDefinitionVersion</code></li>
 *     <li><code>processDefinitionKey</code></li>
 * </ol>
 */
public class ControlHistoricProcessInstanceResponse extends HistoricProcessInstanceResponse {
    protected Integer processDefinitionVersion;
    protected String processDefinitionKey;

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
}
