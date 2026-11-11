/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Opens the decision evaluation view of Flowset Control for the selected decision table.
 * <p>
 * Designed to be attached to a decision-table data grid. Unlike the other Control actions this one has no list
 * destination, so it stays disabled until exactly one row is selected.
 */
@ActionType(EvaluateDmnDecisionInControlAction.ID)
public class EvaluateDmnDecisionInControlAction
        extends AbstractOpenInControlListAction<EvaluateDmnDecisionInControlAction, DmnDecisionData> {

    public static final String ID = "fsc_evaluateDmnDecisionInControl";

    public EvaluateDmnDecisionInControlAction() {
        this(ID);
    }

    public EvaluateDmnDecisionInControlAction(String id) {
        super(id);
        this.icon = VaadinIcon.PLAY.create();
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = EvaluateDmnDecisionInControlAction.class;
        this.text = messages.getMessage(group, "evaluateDmnDecisionInControl.text");
        this.description = messages.getMessage(group, "evaluateDmnDecisionInControl.description");
    }

    /**
     * Evaluation always concerns one decision table, so a single selected row is required. The base class
     * re-evaluates this on every selection change.
     */
    @Override
    protected boolean isApplicable() {
        return super.isApplicable()
                && target != null
                && target.getSingleSelectedItem() != null;
    }

    @Override
    protected String buildDetailUrl(DmnDecisionData item) {
        return controlUrlBuilder.evaluateDecision(item.getId()).build();
    }

    /**
     * Unreachable: {@link #isApplicable()} keeps the action disabled without a selection.
     */
    @Override
    protected String buildListUrl() {
        throw new UnsupportedOperationException(
                "Evaluating a decision requires a selected decision table, there is no list destination");
    }
}
