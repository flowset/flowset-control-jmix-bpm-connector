package io.flowset.controljmixbpm.ui.view.processinstancelist;

import com.vaadin.flow.router.Route;
import io.flowset.controljmixbpm.ui.action.OpenProcessInstanceInControlAction;
import io.jmix.bpmflowui.view.processinstance.ProcessInstanceListView;
import io.jmix.flowui.view.*;

@Route(value = "bpm/process-instances", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_ProcessInstance.list")
@ViewDescriptor(path = "control-process-instance-list-view.xml")
public class ControlProcessInstanceListView extends ProcessInstanceListView {

    @ViewComponent("processInstancesGrid.openInControl")
    protected OpenProcessInstanceInControlAction openInControlAction;

    @Subscribe
    public void onInit(final InitEvent event) {
        // The filter fields belong to the base descriptor and cannot be referenced from the action declaration,
        // so the action is handed them here. It reads their values on every invocation.
        openInControlAction.setFilterFields(processDefinitionComboBox, businessKeyField, processStateComboBox);
    }
}
