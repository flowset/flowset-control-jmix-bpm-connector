/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui.action;

import com.vaadin.flow.component.icon.VaadinIcon;
import io.flowset.controljmixbpm.flowui.ControlJmixBpmUiProperties;
import io.flowset.controljmixbpm.flowui.util.ControlUrlBuilder;
import io.jmix.flowui.action.view.ViewAction;
import io.jmix.flowui.view.StandardDetailView;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

/**
 * Base class for actions that open the Flowset Control page of the entity being edited in a detail view.
 * <p>
 * Declaring the action in the view's {@code <actions>} element is enough to bind it — Jmix sets the target
 * automatically for {@link ViewAction} descendants, and the action then reads the edited entity from it.
 * <p>
 * The action is enabled only while a Control URL is configured and a target view is set, and opens the link in a
 * new browser tab unless {@link #setWindowName(String)} says otherwise.
 *
 * @param <E> the edited entity type
 */
public abstract class AbstractOpenInControlDetailAction<A extends AbstractOpenInControlDetailAction<A, E>, E>
        extends ViewAction<A, StandardDetailView<E>> {

    protected ControlUrlBuilder controlUrlBuilder;
    protected ControlJmixBpmUiProperties controlUiProperties;

    protected String windowName = "_blank";

    protected AbstractOpenInControlDetailAction(String id) {
        super(id);
        this.icon = VaadinIcon.EXTERNAL_LINK.create();
    }

    @Autowired
    public void setControlUrlBuilder(ControlUrlBuilder controlUrlBuilder) {
        this.controlUrlBuilder = controlUrlBuilder;
    }

    @Autowired
    public void setControlUiProperties(ControlJmixBpmUiProperties controlUiProperties) {
        this.controlUiProperties = controlUiProperties;
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
                && controlUrlBuilder != null
                && StringUtils.isNotBlank(controlUiProperties.getControlUrl());
    }

    @Override
    public void execute() {
        checkTarget();

        openInControl(buildUrl(target.getEditedEntity()));
    }

    /**
     * Returns the Control URL of the edited entity.
     */
    protected abstract String buildUrl(E entity);

    protected void openInControl(String url) {
        getCurrentView().getUI().ifPresent(ui -> ui.getPage().open(url, windowName));
    }
}
