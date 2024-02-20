package com.lic.epgs.gratuity.common.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("commonschema")
public class CommonConfigurationProperties {
	private Map<String,String> config;

    public void setConfig(Map<String,String> config) {
        this.config = config;
    }

    public Map<String,String> getConfig() {
        return config;
    }
}
