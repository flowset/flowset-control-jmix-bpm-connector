/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.bpm.entity.ProcessInstanceData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Opens the process instance edited in a detail view in Flowset Control.
 */
@ActionType(OpenProcessInstanceDetailInControlAction.ID)
public class OpenProcessInstanceDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenProcessInstanceDetailInControlAction, ProcessInstanceData> {

    public static final String ID = "ctrcnt_openProcessInstanceDetailInControl";

    public OpenProcessInstanceDetailInControlAction() {
        this(ID);
    }

    public OpenProcessInstanceDetailInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenProcessInstanceDetailInControlAction.class;
        this.text = messages.getMessage(group, "openProcessInstanceDetailInControl.text");
        this.description = messages.getMessage(group, "openProcessInstanceDetailInControl.description");
    }

    @Override
    protected String buildUrl(ProcessInstanceData entity) {
        return controlUrlBuilder.processInstance(entity.getId()).build();
    }
}
