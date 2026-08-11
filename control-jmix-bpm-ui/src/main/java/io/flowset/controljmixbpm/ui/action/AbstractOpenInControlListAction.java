package io.flowset.controljmixbpm.ui.action;

import com.vaadin.flow.component.icon.VaadinIcon;
import io.flowset.controljmixbpm.ui.ControlJmixBpmUiProperties;
import io.flowset.controljmixbpm.ui.util.ControlUrlBuilder;
import io.jmix.flowui.action.list.ListDataComponentAction;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

/**
 * Base class for actions that open a Flowset Control destination derived from a data grid: the selected row's
 * entity when exactly one row is selected, and the corresponding list otherwise.
 * <p>
 * Declaring the action inside a {@code <dataGrid><actions>} element is enough to bind it — Jmix sets the target
 * automatically for {@link ListDataComponentAction} descendants. The action stays enabled regardless of
 * selection; override {@link #isApplicable()} if a selection should be mandatory.
 * <p>
 * The action is enabled only while a Control URL is configured, and opens the link in a new browser tab.
 *
 * @param <E> the grid's item type
 */
public abstract class AbstractOpenInControlListAction<A extends AbstractOpenInControlListAction<A, E>, E>
        extends ListDataComponentAction<A, E> {

    protected ControlUrlBuilder controlUrlBuilder;
    protected ControlJmixBpmUiProperties properties;

    protected boolean openDetails = true;

    protected AbstractOpenInControlListAction(String id) {
        super(id);
        this.icon = VaadinIcon.EXTERNAL_LINK.create();
    }

    @Autowired
    public void setControlUrlBuilder(ControlUrlBuilder controlUrlBuilder) {
        this.controlUrlBuilder = controlUrlBuilder;
    }

    public void setOpenDetails(boolean openDetails) {
        this.openDetails = openDetails;
    }


    @Autowired
    public void setProperties(ControlJmixBpmUiProperties properties) {
        this.properties = properties;
        refreshState();
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
    public void execute() {
        checkTarget();

        E selectedItem = target.getSingleSelectedItem();
        openInControl(selectedItem != null && openDetails
                ? buildDetailUrl(selectedItem)
                : buildListUrl());
    }

    /**
     * Returns the Control URL of a single entity, used when exactly one row is selected.
     */
    protected abstract String buildDetailUrl(E item);

    /**
     * Returns the Control URL of the list, used when no single row is selected. Implementations should apply the
     * view's current filter values, reading them here so that live user input is honoured.
     */
    protected abstract String buildListUrl();

    protected void openInControl(String url) {
        getCurrentView().getUI().ifPresent(ui -> ui.getPage().open(url));
    }
}
