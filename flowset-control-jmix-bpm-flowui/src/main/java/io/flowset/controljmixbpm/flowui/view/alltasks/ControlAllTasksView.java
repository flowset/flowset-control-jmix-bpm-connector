/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.view.alltasks;

import com.vaadin.flow.router.Route;
import io.jmix.bpmflowui.view.alltasks.AllTasksView;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "bpm/all-tasks-view", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_AllTasksView")
@ViewDescriptor(path = "control-all-tasks-view.xml")
public class ControlAllTasksView extends AllTasksView {

}
