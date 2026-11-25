/*
 * Copyright (c) Haulmont 2026. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.controljmixbpm.flowui;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration of the link between this application and a Flowset Control instance.
 */
@ConfigurationProperties(prefix = "flowset.control.jmix-bpm.ui")
public class ControlJmixBpmUiProperties {
    /**
     * The URL of the Flowset Control UI.
     */
    protected String controlUrl;

    /**
     * The URL of the engine that is configured in the Flowset Control UI.
     */
    protected String engineUrl;

    /**
     * Overridden views from Jmix BPM that should not be used in the Jmix application.
     * Required if Jmix BPM is overridden on the application side.
     */
    protected List<String> excludedViews;

    /**
     * Bound by Spring from the {@code flowset.control.jmix-bpm.ui} properties; a parameter is {@code null} when the
     * corresponding property is absent.
     *
     * @param controlUrl    value of {@code flowset.control.jmix-bpm.ui.control-url}
     * @param engineUrl     value of {@code flowset.control.jmix-bpm.ui.engine-url}
     * @param excludedViews value of {@code flowset.control.jmix-bpm.ui.excluded-views}
     */
    public ControlJmixBpmUiProperties(String controlUrl,
                                      String engineUrl,
                                      List<String> excludedViews) {
        this.controlUrl = controlUrl;
        this.engineUrl = engineUrl;
        this.excludedViews = excludedViews;
    }

    /**
     * Returns the base URL of the Flowset Control UI that deep links are built against, as configured by
     * {@code flowset.control.jmix-bpm.ui.control-url}. Blank when this application is not integrated with Control.
     *
     * @return the Control UI URL, blank if none is configured
     */
    public String getControlUrl() {
        return controlUrl;
    }

    /**
     * Sets the base URL of the Flowset Control UI. A trailing slash is tolerated; link building strips it.
     *
     * @param url the Control UI URL
     */
    public void setControlUrl(String url) {
        this.controlUrl = url;
    }

    /**
     * Returns the URL identifying this engine to Control, as configured by {@code flowset.control.jmix-bpm.ui.engine-url}. It is
     * passed to Control as the {@code engineSource} query parameter so Control resolves a link against the right
     * engine. When blank, link building falls back to this application's own context path, which is correct
     * whenever Control reaches the application at the same address the application serves itself on.
     *
     * @return the engine URL registered in Control, blank to fall back to the current context path
     */
    public String getEngineUrl() {
        return engineUrl;
    }

    /**
     * Sets the URL identifying this engine to Control.
     *
     * @param engineUrl the engine URL as registered in Control
     */
    public void setEngineUrl(String engineUrl) {
        this.engineUrl = engineUrl;
    }

    /**
     * Returns the ids of the standard Jmix BPM views this add-on must leave alone, as configured by
     * {@code flowset.control.jmix-bpm.ui.excluded-views}. The add-on works by replacing those views with its own versions
     * carrying the Control buttons; list a view id here when the application replaces it itself, then declare
     * the action in the application's own descriptor.
     *
     * @return the view ids not to override, empty or {@code null} to override all of them
     */
    public List<String> getExcludedViews() {
        return excludedViews;
    }

    /**
     * Sets the ids of the standard Jmix BPM views this add-on must leave alone.
     *
     * @param excludedViews the view ids not to override
     */
    public void setExcludedViews(List<String> excludedViews) {
        this.excludedViews = excludedViews;
    }
}
