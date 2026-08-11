package io.flowset.controljmixbpm.rest.dto.response;

import org.flowable.rest.service.api.runtime.VariableInstanceResponse;

public class ControlVariableInstanceResponse extends VariableInstanceResponse implements HasValueInfo {
    protected VariableValueInfo valueInfo;

    @Override
    public VariableValueInfo getValueInfo() {
        return valueInfo;
    }

    @Override
    public void setValueInfo(VariableValueInfo variableValueInfo) {
        this.valueInfo = variableValueInfo;
    }
}
