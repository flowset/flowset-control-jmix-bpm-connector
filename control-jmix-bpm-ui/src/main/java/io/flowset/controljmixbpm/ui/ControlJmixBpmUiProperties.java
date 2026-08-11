package io.flowset.controljmixbpm.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "flowset.ui")
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

    public ControlJmixBpmUiProperties(String controlUrl, String engineUrl, List<String> excludedViews) {
        this.controlUrl = controlUrl;
        this.engineUrl = engineUrl;
        this.excludedViews = excludedViews;
    }

    /**
     * Returns {@code true} if {@link #getUrl()} is set, i.e. this application is integrated with a Flowset
     * Control instance. The Control actions stay disabled while it is not.
     */
    public boolean isConfigured() {
        return StringUtils.isNotBlank(controlUrl);
    }

    public String getUrl() {
        return controlUrl;
    }

    public void setUrl(String url) {
        this.controlUrl = url;
    }

    public String getEngineUrl() {
        return engineUrl;
    }

    public void setEngineUrl(String engineUrl) {
        this.engineUrl = engineUrl;
    }

    public List<String> getExcludedViews() {
        return excludedViews;
    }

    public void setExcludedViews(List<String> excludedViews) {
        this.excludedViews = excludedViews;
    }
}
