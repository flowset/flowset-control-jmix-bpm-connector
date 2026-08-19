/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Opens the process definition edited in a detail view in Flowset Control.
 */
@ActionType(OpenProcessDefinitionDetailInControlAction.ID)
public class OpenProcessDefinitionDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenProcessDefinitionDetailInControlAction, ProcessDefinitionData> {

    public static final String ID = "ctrcnt_openProcessDefinitionDetailInControl";

    public OpenProcessDefinitionDetailInControlAction() {
        this(ID);
    }

    public OpenProcessDefinitionDetailInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenProcessDefinitionDetailInControlAction.class;
        this.text = messages.getMessage(group, "openProcessDefinitionDetailInControl.text");
        this.description = messages.getMessage(group, "openProcessDefinitionDetailInControl.description");
    }

    @Override
    protected String buildUrl(ProcessDefinitionData entity) {
        return controlUrlBuilder.processDefinition(entity.getId()).build();
    }
}
