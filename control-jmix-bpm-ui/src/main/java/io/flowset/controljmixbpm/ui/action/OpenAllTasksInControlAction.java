package io.flowset.controljmixbpm.ui.action;

import io.jmix.flowui.action.ActionType;

/**
 * Opens the user task list in Flowset Control.
 * <p>
 * Designed to be attached to a button in the all-tasks view. Needs no view context.
 */
@ActionType(OpenAllTasksInControlAction.ID)
public class OpenAllTasksInControlAction extends AbstractOpenInControlAction<OpenAllTasksInControlAction> {

    public static final String ID = "ctrint_openAllTasksInControl";

    public OpenAllTasksInControlAction() {
        this(ID);
    }

    public OpenAllTasksInControlAction(String id) {
        super(id);
    }

    @Override
    protected String buildUrl() {
        return controlUrlBuilder.userTasks().build();
    }
}
