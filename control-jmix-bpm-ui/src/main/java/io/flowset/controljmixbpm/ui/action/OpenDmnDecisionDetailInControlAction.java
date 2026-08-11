package io.flowset.controljmixbpm.ui.action;

import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.flowui.action.ActionType;

/**
 * Opens the decision definition edited in a detail view in Flowset Control.
 */
@ActionType(OpenDmnDecisionDetailInControlAction.ID)
public class OpenDmnDecisionDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenDmnDecisionDetailInControlAction, DmnDecisionData> {

    public static final String ID = "ctrint_openDmnDecisionDetailInControl";

    public OpenDmnDecisionDetailInControlAction() {
        this(ID);
    }

    public OpenDmnDecisionDetailInControlAction(String id) {
        super(id);
    }

    @Override
    protected String buildUrl(DmnDecisionData entity) {
        return controlUrlBuilder.decisionDefinition(entity.getId()).build();
    }
}
