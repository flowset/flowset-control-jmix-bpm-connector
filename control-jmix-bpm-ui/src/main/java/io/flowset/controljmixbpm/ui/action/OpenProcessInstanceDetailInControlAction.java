/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.action;

import io.jmix.bpm.entity.ProcessInstanceData;
import io.jmix.flowui.action.ActionType;

/**
 * Opens the process instance edited in a detail view in Flowset Control.
 */
@ActionType(OpenProcessInstanceDetailInControlAction.ID)
public class OpenProcessInstanceDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenProcessInstanceDetailInControlAction, ProcessInstanceData> {

    public static final String ID = "ctrint_openProcessInstanceDetailInControl";

    public OpenProcessInstanceDetailInControlAction() {
        this(ID);
    }

    public OpenProcessInstanceDetailInControlAction(String id) {
        super(id);
    }

    @Override
    protected String buildUrl(ProcessInstanceData entity) {
        return controlUrlBuilder.processInstance(entity.getId()).build();
    }
}
