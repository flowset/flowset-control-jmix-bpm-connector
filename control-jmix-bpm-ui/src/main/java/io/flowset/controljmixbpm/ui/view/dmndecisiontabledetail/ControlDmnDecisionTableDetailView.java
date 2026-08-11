package io.flowset.controljmixbpm.ui.view.dmndecisiontabledetail;

import com.vaadin.flow.router.Route;
import io.jmix.bpmflowui.view.dmndecisiontable.DmnDecisionTableDetailView;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

/**
 * Adds the "Open in Control" button to the stock decision table detail view. The button and its
 * {@code ctrint_openDmnDecisionDetailInControl} action are declared in the view descriptor.
 */
@Route(value = "bpm/decision-tables/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_DmnDecisionTableDetailView")
@ViewDescriptor(path = "control-dmn-decision-table-detail-view.xml")
public class ControlDmnDecisionTableDetailView extends DmnDecisionTableDetailView {
}
