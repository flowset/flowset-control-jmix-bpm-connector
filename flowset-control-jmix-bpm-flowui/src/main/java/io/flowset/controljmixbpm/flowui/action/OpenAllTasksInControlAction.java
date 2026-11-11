/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Opens the user task list in Flowset Control.
 * <p>
 * Designed to be attached to a button in the all-tasks view. Needs no view context.
 */
@ActionType(OpenAllTasksInControlAction.ID)
public class OpenAllTasksInControlAction extends AbstractOpenInControlAction<OpenAllTasksInControlAction> {

    public static final String ID = "fsc_openAllTasksInControl";

    public OpenAllTasksInControlAction() {
        this(ID);
    }

    public OpenAllTasksInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenAllTasksInControlAction.class;
        this.text = messages.getMessage(group, "openAllTasksInControl.text");
        this.description = messages.getMessage(group, "openAllTasksInControl.description");
    }

    @Override
    protected String buildUrl() {
        return controlUrlBuilder.userTasks().build();
    }
}
