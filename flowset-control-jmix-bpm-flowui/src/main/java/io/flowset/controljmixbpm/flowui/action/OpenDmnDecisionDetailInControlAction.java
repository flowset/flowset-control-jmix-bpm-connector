/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Opens the decision definition edited in a detail view in Flowset Control.
 */
@ActionType(OpenDmnDecisionDetailInControlAction.ID)
public class OpenDmnDecisionDetailInControlAction
        extends AbstractOpenInControlDetailAction<OpenDmnDecisionDetailInControlAction, DmnDecisionData> {

    public static final String ID = "fsc_openDmnDecisionDetailInControl";

    public OpenDmnDecisionDetailInControlAction() {
        this(ID);
    }

    public OpenDmnDecisionDetailInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenDmnDecisionDetailInControlAction.class;
        this.text = messages.getMessage(group, "openDmnDecisionDetailInControl.text");
        this.description = messages.getMessage(group, "openDmnDecisionDetailInControl.description");
    }

    @Override
    protected String buildUrl(DmnDecisionData entity) {
        return controlUrlBuilder.decisionDefinition(entity.getId()).build();
    }
}
