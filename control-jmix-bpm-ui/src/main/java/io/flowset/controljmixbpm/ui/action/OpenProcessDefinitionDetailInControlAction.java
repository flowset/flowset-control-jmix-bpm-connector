package io.flowset.controljmixbpm.ui.action;

import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.flowui.action.ActionType;

/**
 * Opens the process definition edited in a detail view in Flowset Control.
 */
@ActionType(OpenProcessDefinitionDetailInControlAction.ID)
public class OpenProcessDefinitionDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenProcessDefinitionDetailInControlAction, ProcessDefinitionData> {

    public static final String ID = "ctrint_openProcessDefinitionDetailInControl";

    public OpenProcessDefinitionDetailInControlAction() {
        this(ID);
    }

    public OpenProcessDefinitionDetailInControlAction(String id) {
        super(id);
    }

    @Override
    protected String buildUrl(ProcessDefinitionData entity) {
        return controlUrlBuilder.processDefinition(entity.getId()).build();
    }
}
