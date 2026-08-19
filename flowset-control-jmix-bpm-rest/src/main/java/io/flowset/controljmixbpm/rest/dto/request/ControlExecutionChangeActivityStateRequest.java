/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ExecutionChangeActivityStateRequest;

import java.util.List;

/**
 * Extends Flowable's {@link ExecutionChangeActivityStateRequest} and includes process variables.
 */
public class ControlExecutionChangeActivityStateRequest extends ExecutionChangeActivityStateRequest {

    protected List<RestVariable> variables;

    @JsonTypeInfo(use = Id.CLASS, defaultImpl = RestVariable.class)
    public List<RestVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<RestVariable> variables) {
        this.variables = variables;
    }
}
