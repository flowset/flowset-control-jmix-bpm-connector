package io.flowset.controljmixbpm.ui.action;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.flowui.action.ActionType;
import org.apache.commons.lang3.BooleanUtils;
import org.jspecify.annotations.Nullable;

/**
 * Opens decision definitions in Flowset Control.
 * <p>
 * Designed to be attached to a decision-table data grid. When a single row is selected, that decision definition is
 * opened in Control; otherwise the decision-definition list is opened, pre-filtered from the current values of the
 * filter fields set via {@link #setFilterFields(TextField, TextField, Checkbox)}.
 */
@ActionType(OpenDmnDecisionInControlAction.ID)
public class OpenDmnDecisionInControlAction
        extends AbstractOpenInControlListAction<OpenDmnDecisionInControlAction, DmnDecisionData> {

    public static final String ID = "ctrint_openDmnDecisionInControl";

    protected @Nullable TextField nameField;
    protected @Nullable TextField keyField;
    protected @Nullable Checkbox latestVersionOnlyField;

    public OpenDmnDecisionInControlAction() {
        this(ID);
    }

    public OpenDmnDecisionInControlAction(String id) {
        super(id);
    }

    /**
     * Sets the filter fields used to pre-filter the list when no row is selected. Their values are read at
     * execution time, so the current user input is always applied.
     */
    public void setFilterFields(@Nullable TextField nameField,
                                @Nullable TextField keyField,
                                @Nullable Checkbox latestVersionOnlyField) {
        this.nameField = nameField;
        this.keyField = keyField;
        this.latestVersionOnlyField = latestVersionOnlyField;
    }

    @Override
    protected String buildDetailUrl(DmnDecisionData item) {
        return controlUrlBuilder.decisionDefinition(item.getId()).build();
    }

    @Override
    protected String buildListUrl() {
        return controlUrlBuilder.decisionDefinitions()
                .paramIfNotBlank("name", nameField != null ? nameField.getValue() : null)
                .paramIfNotBlank("key", keyField != null ? keyField.getValue() : null)
                // Control lists only the latest version by default, so the parameter is needed only to widen
                // the result to every version.
                .param("latestVersionOnly", isLatestVersionOnly() ? null : false)
                .build();
    }

    protected boolean isLatestVersionOnly() {
        return latestVersionOnlyField != null && BooleanUtils.isTrue(latestVersionOnlyField.getValue());
    }
}
