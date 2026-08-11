/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.action;

import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.flowui.action.ActionType;

import java.util.function.Supplier;

/**
 * Opens process definitions in Flowset Control.
 * <p>
 * Designed to be attached to a process-definition data grid. When a single row is selected, that process
 * definition is opened in Control; otherwise the process-definition list is opened, pre-filtered from the current
 * values of the filter fields.
 */
@ActionType(OpenProcessDefinitionInControlAction.ID)
public class OpenProcessDefinitionInControlAction
        extends AbstractOpenInControlListAction<OpenProcessDefinitionInControlAction, ProcessDefinitionData> {

    public static final String ID = "ctrint_openProcessDefinitionInControl";

    protected Supplier<String> nameFilterProvider;
    protected Supplier<String> keyFilterProvider;
    protected Supplier<Boolean> latestVersionFilterProvider;



    public OpenProcessDefinitionInControlAction() {
        this(ID);
    }

    public OpenProcessDefinitionInControlAction(String id) {
        super(id);
    }

    public void setNameFilterProvider(Supplier<String> nameFilterProvider) {
        this.nameFilterProvider = nameFilterProvider;
    }

    public void setKeyFilterProvider(Supplier<String> keyFilterProvider) {
        this.keyFilterProvider = keyFilterProvider;
    }

    public void setLatestVersionFilterProvider(Supplier<Boolean> latestVersionFilterProvider) {
        this.latestVersionFilterProvider = latestVersionFilterProvider;
    }



    @Override
    protected String buildDetailUrl(ProcessDefinitionData item) {
        return controlUrlBuilder.processDefinition(item.getId()).build();
    }

    @Override
    protected String buildListUrl() {
        return controlUrlBuilder.processDefinitions()
                .paramIfNotBlank("name", nameFilterProvider != null ? nameFilterProvider.get() : null)
                .paramIfNotBlank("key", keyFilterProvider != null ? keyFilterProvider.get() : null)
                .param("latestVersionOnly", latestVersionFilterProvider != null ? latestVersionFilterProvider.get() : null)
                .build();
    }
}
