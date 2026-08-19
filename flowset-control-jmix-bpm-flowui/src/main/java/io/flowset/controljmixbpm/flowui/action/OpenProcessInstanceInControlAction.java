/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.bpm.entity.ProcessInstanceData;
import io.jmix.core.Messages;
import io.jmix.flowui.action.ActionType;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

/**
 * Opens process instances in Flowset Control.
 * <p>
 * Designed to be attached to a process-instance data grid. When a single row is selected, that process instance is
 * opened in Control; otherwise the process-instance list is opened, pre-filtered from the current values of the
 * filter fields.
 */
@ActionType(OpenProcessInstanceInControlAction.ID)
public class OpenProcessInstanceInControlAction
        extends AbstractOpenInControlListAction<OpenProcessInstanceInControlAction, ProcessInstanceData> {

    public static final String ID = "ctrcnt_openProcessInstanceInControl";

    /**
     * Completion-state values of the BPM view's state filter.
     */
    protected static final String STATE_UNFINISHED = "unfinished";
    protected static final String STATE_FINISHED = "finished";

    /**
     * Corresponding values of Control's {@code mode} query parameter.
     */
    protected static final String MODE_ACTIVE = "active";
    protected static final String MODE_COMPLETED = "completed";
    protected static final String MODE_ALL = "all";

    protected Supplier<ProcessDefinitionData> processFilterValueProvider;
    protected Supplier<String> businessKeyFilterValueProvider;
    protected Supplier<String> stateFilterValueProvider;

    public OpenProcessInstanceInControlAction() {
        this(ID);
    }

    public OpenProcessInstanceInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the action's own text and tooltip. The message group is taken from the class literal rather
     * than from {@code getClass()} so that it stays in this package when the action is subclassed.
     */
    @Autowired
    protected void setMessages(Messages messages) {
        Class<?> group = OpenProcessInstanceInControlAction.class;
        this.text = messages.getMessage(group, "openProcessInstanceInControl.text");
        this.description = messages.getMessage(group, "openProcessInstanceInControl.description");
    }

    public void setProcessFilterValueProvider(Supplier<ProcessDefinitionData> processFilterValueProvider) {
        this.processFilterValueProvider = processFilterValueProvider;
    }

    public void setBusinessKeyFilterValueProvider(Supplier<String> businessKeyFilterValueProvider) {
        this.businessKeyFilterValueProvider = businessKeyFilterValueProvider;
    }

    public void setStateFilterValueProvider(Supplier<String> stateFilterValueProvider) {
        this.stateFilterValueProvider = stateFilterValueProvider;
    }

    @Override
    protected String buildDetailUrl(ProcessInstanceData item) {
        return controlUrlBuilder.processInstance(item.getId()).build();
    }

    @Override
    protected String buildListUrl() {
        return controlUrlBuilder.processInstances()
                .paramIfNotBlank("processKey", resolveProcessKey())
                .paramIfNotBlank("businessKey", businessKeyFilterValueProvider != null ? businessKeyFilterValueProvider.get() : null)
                .paramIfNotBlank("mode", resolveMode())
                .build();
    }

    protected @Nullable String resolveProcessKey() {
        if (processFilterValueProvider == null) {
            return null;
        }
        ProcessDefinitionData processDefinition = processFilterValueProvider.get();
        return processDefinition != null ? processDefinition.getKey() : null;
    }

    /**
     * Maps the view's completion-state filter onto Control's {@code mode} parameter. With no state selected the
     * list is widened to every instance; an unrecognised state leaves the mode to Control's own default.
     */
    protected @Nullable String resolveMode() {
        String state = stateFilterValueProvider != null ? stateFilterValueProvider.get() : null;
        if (StringUtils.isBlank(state)) {
            return MODE_ALL;
        }
        return switch (state) {
            case STATE_UNFINISHED -> MODE_ACTIVE;
            case STATE_FINISHED -> MODE_COMPLETED;
            default -> null;
        };
    }
}
