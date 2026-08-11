/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.ui.util;

import io.flowset.controljmixbpm.ui.ControlJmixBpmUiProperties;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Builds deep links into Flowset Control.
 * <p>
 * This is the single place that knows Control's routes and the {@code engineSource} query parameter identifying
 * the engine a link should be resolved against. The engine is taken from {@code flowset.control.engine-url} and
 * falls back to the current application's context path, so a deployment that is reachable at the same address
 * from Control does not need to configure anything beyond {@code flowset.control.url}.
 * <p>
 * Example of usage:
 * <pre>{@code
 * String detail = controlUrlBuilder.processInstance(id).build();
 *
 * String filteredList = controlUrlBuilder.processInstances()
 *         .paramIfNotBlank("businessKey", businessKey)
 *         .param("mode", "active")
 *         .build();
 * }</pre>
 * Use {@link #route(String)} for a Control route not covered by a convenience method.
 */
@Component("ctrint_ControlUrlBuilder")
public class ControlUrlBuilder {

    public static final String PROCESS_DEFINITIONS_ROUTE = "bpm/process-definitions";
    public static final String PROCESS_INSTANCES_ROUTE = "bpm/process-instances";
    public static final String USER_TASKS_ROUTE = "bpm/user-tasks";
    public static final String DECISION_DEFINITIONS_ROUTE = "bpm/decision-definitions";
    public static final String EVALUATE_DECISION_ROUTE = "evaluate-decision";

    public static final String ENGINE_SOURCE_PARAM = "engineSource";

    protected final ControlJmixBpmUiProperties controlUiProperties;

    public ControlUrlBuilder(ControlJmixBpmUiProperties controlUiProperties) {
        this.controlUiProperties = controlUiProperties;
    }

    /**
     * Starts building the URL of the process definition list.
     */
    public Builder processDefinitions() {
        return route(PROCESS_DEFINITIONS_ROUTE);
    }

    /**
     * Starts building the URL of a single process definition.
     */
    public Builder processDefinition(String id) {
        return processDefinitions().id(id);
    }

    /**
     * Starts building the URL of the process instance list.
     */
    public Builder processInstances() {
        return route(PROCESS_INSTANCES_ROUTE);
    }

    /**
     * Starts building the URL of a single process instance.
     */
    public Builder processInstance(String id) {
        return processInstances().id(id);
    }

    /**
     * Starts building the URL of the user task list.
     */
    public Builder userTasks() {
        return route(USER_TASKS_ROUTE);
    }

    /**
     * Starts building the URL of the decision definition list.
     */
    public Builder decisionDefinitions() {
        return route(DECISION_DEFINITIONS_ROUTE);
    }

    /**
     * Starts building the URL of a single decision definition.
     */
    public Builder decisionDefinition(String id) {
        return decisionDefinitions().id(id);
    }

    /**
     * Starts building the URL of the decision evaluation view for a single decision definition.
     */
    public Builder evaluateDecision(String id) {
        return route(EVALUATE_DECISION_ROUTE).id(id);
    }

    /**
     * Starts building a URL for the given Control route. The route is a slash-separated path relative to the
     * configured Control URL, e.g. {@link #PROCESS_INSTANCES_ROUTE}.
     */
    public Builder route(String route) {
        return new Builder(route);
    }

    /**
     * Resolves the engine the built links point at: the explicitly configured engine URL, or this application's
     * own context path when none is configured.
     */
    protected String resolveEngineSource() {
        String engineUrl = controlUiProperties.getEngineUrl();
        if (StringUtils.isNotBlank(engineUrl)) {
            return engineUrl;
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    /**
     * Collects path segments and query parameters for a single Control URL. Obtain one via
     * {@link ControlUrlBuilder#route(String)}.
     */
    public class Builder {

        protected final UriComponentsBuilder uriBuilder;

        protected Builder(String route) {
            if (!controlUiProperties.isConfigured()) {
                throw new IllegalStateException(
                        "Control URL is not configured, cannot build a link. Set 'flowset.control.url' " +
                                "or check ControlJmixBpmUiProperties.isConfigured() before building.");
            }
            this.uriBuilder = UriComponentsBuilder
                    .fromUriString(StringUtils.stripEnd(controlUiProperties.getUrl(), "/"))
                    .path("/" + route);
        }

        /**
         * Appends an entity identifier as the last path segment.
         */
        public Builder id(String id) {
            uriBuilder.pathSegment(id);
            return this;
        }

        /**
         * Adds a query parameter. A {@code null} value is skipped, so optional filters can be passed
         * unconditionally.
         */
        public Builder param(String name, @Nullable Object value) {
            if (value != null) {
                uriBuilder.queryParam(name, value);
            }
            return this;
        }

        /**
         * Adds a query parameter unless the value is {@code null}, empty or whitespace-only. Intended for filter
         * fields, which report blank rather than {@code null} when the user has not typed anything.
         */
        public Builder paramIfNotBlank(String name, @Nullable String value) {
            if (StringUtils.isNotBlank(value)) {
                uriBuilder.queryParam(name, value);
            }
            return this;
        }

        /**
         * Returns the absolute Control URL, with {@code engineSource} appended and every component encoded.
         */
        public String build() {
            return uriBuilder
                    .queryParam(ENGINE_SOURCE_PARAM, resolveEngineSource())
                    // encode() escapes only characters that are illegal in their component, so a URL-valued
                    // engineSource keeps its ':' and '/' literal.
                    .build()
                    .encode()
                    .toUriString();
        }
    }
}
