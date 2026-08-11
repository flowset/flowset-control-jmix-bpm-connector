/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.view.processinstancedetail;

import com.vaadin.flow.router.Route;
import io.jmix.bpmflowui.view.processinstance.ProcessInstanceDetailView;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

/**
 * Adds the "Open in Control" button to the stock process instance detail view. The button and its
 * {@code ctrint_openProcessInstanceDetailInControl} action are declared in the view descriptor.
 */
@Route(value = "bpm/process-instances/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_ProcessInstanceData.detail")
@ViewDescriptor(path = "control-process-instance-detail-view.xml")
public class ControlProcessInstanceDetailView extends ProcessInstanceDetailView {
}
