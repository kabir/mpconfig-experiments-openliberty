package io.openliberty.guides.multimodules.ejb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.Config;


@Stateless
public class StatelessSessionBean {
    @Inject
    Config config;

    public Config getConfig() {
        System.out.println("StatelessSessionBean: " + this.getClass().getClassLoader());
        return config;
    }
}
