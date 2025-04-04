package io.openliberty.guides.multimodules.ejb;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;


@Stateless
public class SessionBean {
    @Inject
    Config config;

    public Config getConfig() {
        System.out.println("LibBean: " + this.getClass().getClassLoader());
        return config;
    }
}
