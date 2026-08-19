/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.request;

import org.flowable.common.rest.util.DateToStringSerializer;
import org.flowable.rest.service.api.history.HistoricActivityInstanceQueryRequest;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Extends Flowable's {@link HistoricActivityInstanceQueryRequest} and includes additional options for filtering:
 * <ol>
 *     <li>filter by finished date: <code>finishedBefore</code> and <code>finishedAfter</code></li>
 * </ol>
 */
public class ControlHistoricActivityInstanceQueryRequest extends HistoricActivityInstanceQueryRequest {
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date finishedBefore;

    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date finishedAfter;

    public Date getFinishedBefore() {
        return finishedBefore;
    }

    public void setFinishedBefore(Date finishedBefore) {
        this.finishedBefore = finishedBefore;
    }

    public Date getFinishedAfter() {
        return finishedAfter;
    }

    public void setFinishedAfter(Date finishedAfter) {
        this.finishedAfter = finishedAfter;
    }
}
