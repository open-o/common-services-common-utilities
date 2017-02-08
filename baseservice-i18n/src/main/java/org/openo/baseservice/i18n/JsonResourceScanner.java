/**
 * Copyright 2017 ZTE Corporation.
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

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

final class JsonResourceScanner {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(JsonResourceScanner.class);
  private static final Pattern PATTERN_OF_I18N_FILE_NAME =
      Pattern.compile(".*?\\-i18n\\-.*?\\.json");
  private static final Pattern PATTERN_OF_ERROR_CODE_FILE_NAME =
      Pattern.compile(".*?\\-errorcode\\-.*?\\.json");

  private static Collection<String> findFromClassPath(Pattern pattern) {
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    Collection<URL> urls = ClasspathHelper.forJavaClassPath();

    for (Iterator<URL> iter = urls.iterator(); iter.hasNext();) {
      URL url = iter.next();
      boolean exist = false;
      try {
        exist = new File(url.getFile()).exists();
        if (!exist) {
          LOG.info("class path url ignored for not exists: " + url.toString());
        }
      } catch (Exception e) {
        LOG.info("class path url ignored for exception: " + url.toString(), e);
        exist = false;
      }
      if (!exist) {
        iter.remove();
      }
    }
    for (URL url : urls) {
      LOG.info("class path url:" + url.toString());
    }
    configurationBuilder.addUrls(urls);
    configurationBuilder.setScanners(new ResourcesScanner());
    configurationBuilder.useParallelExecutor();
    Reflections reflections = new Reflections(configurationBuilder);
    Set<String> results = reflections.getResources(pattern);
    if (results == null) {
      return Collections.emptySet();
    } else {
      return results;
    }
  }

  static Collection<String> findI18nPaths() {
    return findFromClassPath(PATTERN_OF_I18N_FILE_NAME);
  }

  static Collection<String> findErrorCodePaths() {
    return findFromClassPath(PATTERN_OF_ERROR_CODE_FILE_NAME);
  }
}
