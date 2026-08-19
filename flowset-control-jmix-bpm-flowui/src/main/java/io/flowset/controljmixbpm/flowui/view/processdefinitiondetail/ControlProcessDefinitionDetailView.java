/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.view.processdefinitiondetail;

import com.vaadin.flow.router.Route;
import io.jmix.bpmflowui.view.processdefinition.ProcessDefinitionDetailView;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

/**
 * Adds the "Open in Control" button to the stock process definition detail view. The button and its
 * {@code ctrcnt_openProcessDefinitionDetailInControl} action are declared in the view descriptor.
 */
@Route(value = "bpm/process-definitions/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_ProcessDefinition.detail")
@ViewDescriptor(path = "control-process-definition-detail-view.xml")
public class ControlProcessDefinitionDetailView extends ProcessDefinitionDetailView {
}
