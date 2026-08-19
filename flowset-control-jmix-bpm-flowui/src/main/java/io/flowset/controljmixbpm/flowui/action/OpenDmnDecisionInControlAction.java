/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

/**
 * Opens decision definitions in Flowset Control.
 * <p>
 * Designed to be attached to a decision-table data grid. When a single row is selected, that decision definition is
 * opened in Control; otherwise the decision-definition list is opened, pre-filtered from the current values of the
 * filter fields. Every provider below is invoked on each execution, so the current user input is always applied.
 */
@ActionType(OpenDmnDecisionInControlAction.ID)
public class OpenDmnDecisionInControlAction
        extends AbstractOpenInControlListAction<OpenDmnDecisionInControlAction, DmnDecisionData> {

    public static final String ID = "ctrcnt_openDmnDecisionInControl";

    protected Supplier<String> nameFilterProvider;
    protected Supplier<String> keyFilterProvider;
    protected Supplier<Boolean> latestVersionFilterProvider;

    public OpenDmnDecisionInControlAction() {
        this(ID);
    }

    public OpenDmnDecisionInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenDmnDecisionInControlAction.class;
        this.text = messages.getMessage(group, "openDmnDecisionInControl.text");
        this.description = messages.getMessage(group, "openDmnDecisionInControl.description");
    }

    /**
     * Sets the provider of the decision name to pre-filter the list with. A blank value is not passed on.
     */
    public void setNameFilterProvider(Supplier<String> nameFilterProvider) {
        this.nameFilterProvider = nameFilterProvider;
    }

    /**
     * Sets the provider of the decision key to pre-filter the list with. A blank value is not passed on.
     */
    public void setKeyFilterProvider(Supplier<String> keyFilterProvider) {
        this.keyFilterProvider = keyFilterProvider;
    }

    /**
     * Sets the provider deciding whether the list is restricted to the latest version of every decision. A
     * {@code null} value leaves the restriction to Control's own default.
     */
    public void setLatestVersionFilterProvider(Supplier<Boolean> latestVersionFilterProvider) {
        this.latestVersionFilterProvider = latestVersionFilterProvider;
    }

    @Override
    protected String buildDetailUrl(DmnDecisionData item) {
        return controlUrlBuilder.decisionDefinition(item.getId()).build();
    }

    @Override
    protected String buildListUrl() {
        return controlUrlBuilder.decisionDefinitions()
                .paramIfNotBlank("name", nameFilterProvider != null ? nameFilterProvider.get() : null)
                .paramIfNotBlank("key", keyFilterProvider != null ? keyFilterProvider.get() : null)
                .param("latestVersionOnly", latestVersionFilterProvider != null ? latestVersionFilterProvider.get() : null)
                .build();
    }
}
