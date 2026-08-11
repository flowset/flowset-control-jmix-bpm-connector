/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.action;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.flowset.controljmixbpm.ui.ControlJmixBpmUiProperties;
import io.flowset.controljmixbpm.ui.util.ControlUrlBuilder;
import io.jmix.flowui.action.SecuredBaseAction;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

/**
 * Base class for actions that open a fixed Flowset Control destination, i.e. one that needs no context from the
 * view the action is attached to.
 * <p>
 * The action is enabled only while a Control URL is configured, and opens the link in a new browser tab unless
 * {@link #setWindowName(String)} says otherwise.
 *
 * @see AbstractOpenInControlListAction for a destination derived from a data grid selection
 * @see AbstractOpenInControlDetailAction for a destination derived from a detail view's edited entity
 */
public abstract class AbstractOpenInControlAction<A extends AbstractOpenInControlAction<A>>
        extends SecuredBaseAction<A> {

    protected ControlUrlBuilder controlUrlBuilder;
    protected ControlJmixBpmUiProperties properties;

    protected String windowName = "_blank";

    protected AbstractOpenInControlAction(String id) {
        super(id);
        this.icon = VaadinIcon.EXTERNAL_LINK.create();
    }

    @Autowired
    public void setControlUrlBuilder(ControlUrlBuilder controlUrlBuilder) {
        this.controlUrlBuilder = controlUrlBuilder;
    }

    @Autowired
    public void setProperties(ControlJmixBpmUiProperties properties) {
        this.properties = properties;
        refreshState();
    }

    /**
     * Sets the browser window the link is opened in. Defaults to {@code "_blank"}; {@code null}, an empty string
     * and {@code "_self"} all navigate the current window instead.
     */
    public void setWindowName(@Nullable String windowName) {
        this.windowName = windowName;
    }

    /**
     * The action is permitted only when a Control URL is configured.
     */
    @Override
    protected boolean isPermitted() {
        return super.isPermitted()
                && properties != null
                && properties.isConfigured();
    }

    @Override
    public void actionPerform(Component component) {
        openInControl(buildUrl());
    }

    /**
     * Returns the Control URL to open. Called on every invocation, so implementations may read live view state.
     */
    protected abstract String buildUrl();

    protected void openInControl(String url) {
        getCurrentView().getUI().ifPresent(ui -> ui.getPage().open(url, windowName));
    }
}
