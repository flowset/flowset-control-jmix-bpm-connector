package io.flowset.controljmixbpm.ui.view.processdefinitionlist;

import com.vaadin.flow.router.Route;
import io.flowset.controljmixbpm.ui.action.OpenProcessDefinitionInControlAction;
import io.jmix.bpmflowui.view.processdefinition.ProcessDefinitionListView;
import io.jmix.flowui.view.*;

@Route(value = "bpm/process-definitions", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_ProcessDefinition.list")
@ViewDescriptor(path = "control-process-definition-list-view.xml")
public class ControlProcessDefinitionListView extends ProcessDefinitionListView {

    @ViewComponent("processDefinitionsGrid.openInControl")
    protected OpenProcessDefinitionInControlAction openInControlAction;

    @Subscribe
    public void onInit(final InitEvent event) {
        // The filter fields belong to the base descriptor and cannot be referenced from the action declaration,
        // so the action is handed them here. It reads their values on every invocation.
        openInControlAction.setKeyFilterProvider(() -> keyField.getValue());
        openInControlAction.setNameFilterProvider(() -> nameField.getValue());
        openInControlAction.setLatestVersionFilterProvider(() -> lastVersionOnlyCb.getValue());
    }
}
