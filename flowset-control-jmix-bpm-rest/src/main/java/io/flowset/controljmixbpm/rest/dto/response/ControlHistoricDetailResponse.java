/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.response;

import org.flowable.rest.service.api.history.HistoricDetailResponse;

/**
 * Extends Flowable's {@link HistoricDetailResponse} and includes additional fields:
 * <ol>
 *     <li><code>valueInfo</code> - additional information for entity, entity list, and file description variables</li>
 * </ol>
 */
public class ControlHistoricDetailResponse extends HistoricDetailResponse implements HasValueInfo {
    protected VariableValueInfo valueInfo;

    public VariableValueInfo getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(VariableValueInfo valueInfo) {
        this.valueInfo = valueInfo;
    }
}
