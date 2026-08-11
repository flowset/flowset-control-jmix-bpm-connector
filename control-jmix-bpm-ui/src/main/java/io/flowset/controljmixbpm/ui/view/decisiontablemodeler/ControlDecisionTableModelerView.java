/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.view.decisiontablemodeler;

import com.vaadin.flow.router.Route;
import io.jmix.bpm.entity.DmnDecisionData;
import io.jmix.bpm.entity.ProcessDefinitionData;
import io.jmix.bpmflowui.view.decisiontablemodeler.DecisionTableModelerView;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.flowui.view.navigation.UrlParamSerializer;
import org.apache.commons.io.IOUtils;
import org.flowable.dmn.api.DmnDecisionQuery;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Route(value = "bpm/dmn-modeler-view", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_DecisionTableModelerView")
@ViewDescriptor(path = "control-decision-table-modeler-view.xml")
public class ControlDecisionTableModelerView extends DecisionTableModelerView {
    private static final Logger log = LoggerFactory.getLogger(ControlDecisionTableModelerView.class);

    public static final String DECISION_DEF_ID_URL_QUERY_PARAM = "decisionDefId";
    @Autowired
    private UrlParamSerializer urlParamSerializer;

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
        Map<String, List<String>> queryParametersMap = event.getQueryParameters().getParameters();
         if (queryParametersMap.containsKey(DECISION_DEF_ID_URL_QUERY_PARAM)) {
            event.getQueryParameters()
                    .getSingleParameter(DECISION_DEF_ID_URL_QUERY_PARAM)
                    .map(p -> urlParamSerializer.deserialize(String.class, p))
                    .ifPresent(this::setupDecisionDefinition);
        }
    }

    protected void setupDecisionDefinition(String decisionDefId) {
        DmnDecisionQuery processDefQuery = dmnRepositoryService.createDecisionQuery()
                .decisionId(decisionDefId);
        if (bpmTenantProvider != null && bpmTenantProvider.isMultitenancyActive()) {
            processDefQuery.decisionTenantId(bpmTenantProvider.getCurrentUserTenantId());
        }

        DmnDecisionData decisionDef = entitiesConverter.createDmnDecisionTableData(processDefQuery.singleResult());
        if (decisionDef == null) {
            notifications.create(
                            messages.getMessage(DecisionTableModelerView.class, "decisionDefinitionNotFound.notification.title"),
                            messages.formatMessage(DecisionTableModelerView.class, "decisionDefinitionNotFound.notification.message",
                                    decisionDefId))
                    .withType(Notifications.Type.ERROR)
                    .show();
            return;
        }

        try (InputStream is = dmnRepositoryService.getDmnResource(decisionDefId)) {
            String dmnXml = IOUtils.toString(is, StandardCharsets.UTF_8);
            setDmnXml(dmnXml);
        } catch (IOException e) {
            log.error("Cannot read model from process definition: {}", decisionDefId, e);
            notifications.create(
                            messages.getMessage(DecisionTableModelerView.class, "cannotReadModelFromDecisionDefinition.notification.title"),
                            messages.formatMessage(DecisionTableModelerView.class, "cannotReadModelFromDecisionDefinition.notification.message",
                                    decisionDefId))
                    .withType(Notifications.Type.ERROR)
                    .show();
        }
    }
}