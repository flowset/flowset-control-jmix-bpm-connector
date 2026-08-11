/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.action;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.bpm.entity.ProcessInstanceData;
import io.jmix.flowui.action.ActionType;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

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

    public static final String ID = "ctrint_openProcessInstanceInControl";

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

    protected @Nullable ComboBox<ProcessDefinitionData> processDefinitionField;
    protected @Nullable TextField businessKeyField;
    protected @Nullable ComboBox<String> stateField;

    public OpenProcessInstanceInControlAction() {
        this(ID);
    }

    public OpenProcessInstanceInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the filter fields used to pre-filter the list when no row is selected. Their values are read at
     * execution time, so the current user input is always applied.
     */
    public void setFilterFields(@Nullable ComboBox<ProcessDefinitionData> processDefinitionField,
                                @Nullable TextField businessKeyField,
                                @Nullable ComboBox<String> stateField) {
        this.processDefinitionField = processDefinitionField;
        this.businessKeyField = businessKeyField;
        this.stateField = stateField;
    }

    @Override
    protected String buildDetailUrl(ProcessInstanceData item) {
        return controlUrlBuilder.processInstance(item.getId()).build();
    }

    @Override
    protected String buildListUrl() {
        return controlUrlBuilder.processInstances()
                .paramIfNotBlank("processKey", resolveProcessKey())
                .paramIfNotBlank("businessKey", businessKeyField != null ? businessKeyField.getValue() : null)
                .paramIfNotBlank("mode", resolveMode())
                .build();
    }

    protected @Nullable String resolveProcessKey() {
        if (processDefinitionField == null) {
            return null;
        }
        ProcessDefinitionData processDefinition = processDefinitionField.getValue();
        return processDefinition != null ? processDefinition.getKey() : null;
    }

    /**
     * Maps the view's completion-state filter onto Control's {@code mode} parameter. With no state selected the
     * list is widened to every instance; an unrecognised state leaves the mode to Control's own default.
     */
    protected @Nullable String resolveMode() {
        String state = stateField != null ? stateField.getValue() : null;
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
