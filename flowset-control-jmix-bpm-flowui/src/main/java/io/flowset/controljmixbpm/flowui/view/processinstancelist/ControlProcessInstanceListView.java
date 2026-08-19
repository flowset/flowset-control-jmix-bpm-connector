/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.view.processinstancelist;

import com.vaadin.flow.router.Route;
import io.flowset.controljmixbpm.flowui.action.OpenProcessInstanceInControlAction;
import io.jmix.bpmflowui.view.processinstance.ProcessInstanceListView;
import io.jmix.flowui.view.*;

@Route(value = "bpm/process-instances", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_ProcessInstance.list")
@ViewDescriptor(path = "control-process-instance-list-view.xml")
public class ControlProcessInstanceListView extends ProcessInstanceListView {

    @ViewComponent("processInstancesGrid.openInControl")
    protected OpenProcessInstanceInControlAction openInControlAction;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        initActions();
    }

    protected void initActions() {

        openInControlAction.setBusinessKeyFilterValueProvider(() -> businessKeyField.getValue());
        openInControlAction.setProcessFilterValueProvider(() -> processDefinitionComboBox.getValue());
        openInControlAction.setStateFilterValueProvider(() -> processStateComboBox.getValue());
    }

}
