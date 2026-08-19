/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.rest.dto.response;

/**
 * Marks interface for classes containing process variable value information.
 */
public interface HasValueInfo {
    /**
     *
     * @return returns additional information about the variable value
     */
    VariableValueInfo getValueInfo();

    /**
     * Sets additional information about the variable value
     *
     * @param variableValueInfo additional information about the variable value
     */
    void setValueInfo(VariableValueInfo variableValueInfo);
}
