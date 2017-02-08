/**
 * Copyright 2016 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.i18n;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;
import java.util.regex.Pattern;

public class ReflectionTest {
  public static void main(String[] args) throws Exception {
    // for(URL url:ClasspathHelper.forJavaClassPath()){
    // System.out.println(url);
    // }


    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    configurationBuilder.addUrls(ClasspathHelper.forJavaClassPath());
    // configurationBuilder.addUrls(ClasspathHelper.forClassLoader());
    // configurationBuilder.addUrls(ClasspathHelper.forManifest());

    configurationBuilder.setScanners(new ResourcesScanner());
    Reflections reflections = new Reflections(configurationBuilder);
    Set<String> properties = reflections.getResources(Pattern.compile(".*\\.json"));
    for (String s : properties) {
      System.out.println(s);
      System.out.println(ClassLoader.getSystemResource(s));
    }

  }

}
