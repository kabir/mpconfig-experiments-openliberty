// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.multimodules.web;

import java.util.Map;
import java.util.TreeMap;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.openliberty.guides.multimodules.ejb.StatelessSessionBean;
import io.openliberty.guides.multimodules.lib.LibBean;
import io.openliberty.guides.multimodules.lib.WebInfJarBean;
import org.eclipse.microprofile.config.Config;

@RequestScoped
@Path("/")
public class SampleResource {

  // tag::config[]
  @Inject
  private Config config;
  // end::config[]

  @Inject
  private WebInfJarBean webInfJarBean;

  @Inject
  private LibBean libBean;

//  @EJB
//  private StatelessSessionBean sessionBean;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject getAllConfig() {
    System.out.println("SampleResource: " + this.getClass().getClassLoader());

    JsonObjectBuilder builder = Json.createObjectBuilder();
    return builder
            .add("WarConfig", propertyJsonBuilder("war", config))
            .add("WebInfJarConfig", propertyJsonBuilder("webinf", webInfJarBean.getConfig()))
            .add("LibBean", propertyJsonBuilder("lib", libBean.getConfig()))
            //.add("SLSB", propertyJsonBuilder("slsb", sessionBean.getConfig()))
            .build();
  }

  public JsonObject propertyJsonBuilder(String archiveName, Config config) {
    System.out.println("=====> " + archiveName);
    JsonObjectBuilder propertiesBuilder = Json.createObjectBuilder();
    Map<String, Object> map = new TreeMap<>();
    for (String name : config.getPropertyNames()) {
      if (name.startsWith("sanity.test.")) {
        map.put(name, config.getValue(name, String.class));
        propertiesBuilder.add(name, config.getValue(name, String.class));
      }
    }
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
    return propertiesBuilder.build();
  }
}
