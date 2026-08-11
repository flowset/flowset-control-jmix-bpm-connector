package io.flowset.controljmixbpm.ui.view.dmndecisiontablelist;

import com.vaadin.flow.router.Route;
import io.flowset.controljmixbpm.ui.action.OpenDmnDecisionInControlAction;
import io.jmix.bpmflowui.view.dmndecisiontable.DmnDecisionTableListView;
import io.jmix.flowui.view.*;

@Route(value = "bpm/decision-tables", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_DmnDecisionTableListView")
@ViewDescriptor(path = "control-dmn-decision-table-list-view.xml")
public class ControlDmnDecisionTableListView extends DmnDecisionTableListView {

    @ViewComponent("dmnDecisionTablesGrid.openInControl")
    protected OpenDmnDecisionInControlAction openInControlAction;

    @Subscribe
    public void onInit(final InitEvent event) {
        // The filter fields belong to the base descriptor and cannot be referenced from the action declaration,
        // so the action is handed them here. It reads their values on every invocation.
        openInControlAction.setFilterFields(nameField, keyField, lastVersionOnlyCb);
    }
}
