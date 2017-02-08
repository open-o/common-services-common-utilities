/**
 * Copyright 2017 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class DefaultErrorCodeI18n implements ErrorCodeI18n {

    static final Logger logger = LoggerFactory.getLogger(DefaultErrorCodeI18n.class);
    private static DefaultErrorCodeI18n singleton;
    private static final Lock lock = new ReentrantLock();
    private Map<Integer, ErrorItemImpl> errorItems;

    private DefaultErrorCodeI18n() {
        try {
            init();
        } catch (Exception e) {
            logger.error("init ErrorCodeI18n failed.", e);
        }
    }

    private void init() throws Exception {
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        final Map<Integer, ErrorItemImpl> errorItems = new HashMap<Integer, DefaultErrorCodeI18n.ErrorItemImpl>();
        JsonResourceScanner.findErrorCodePaths().forEach(path -> {
            HashMap<String, Object> fileValues = null;
            try (InputStream ins = systemClassLoader.getResourceAsStream(path)) {
                fileValues = I18nJsonUtil.getInstance().readFromJson(ins, HashMap.class);
                logger.info("load errorcode file success: " + path);
            } catch (IOException ex) {
                logger.info("load errorcode file failed: " + path);
                logger.info(
                        "load errorcode file failed: " + systemClassLoader.getResource(path).toString(),
                        ex);
                return;
            }
            List<?> errcodes = (List<?>) fileValues.get("errcodes");
            if (errcodes == null) {
                logger.info("none errcodes field in: " + path);
                return;
            }

            String fileName = null;
            int i = path.lastIndexOf("/");
            if (i > -1) {
                fileName = path.substring(i + 1);
            } else {
                fileName = path;
            }
            i = fileName.indexOf("-errorcode-");
            String localeSrc = fileName.substring(i + 11, fileName.lastIndexOf("."));
            if (localeSrc.isEmpty()) {
                logger.info("parse errorcode file failed: locale is null");
                return;
            }

            String[] ss = localeSrc.replace("-", "_").split("_");
            String tempLocale = null;
            if (ss.length == 1) {
                tempLocale = new Locale(ss[0]).toString();
            } else if (ss.length == 2) {
                tempLocale = new Locale(ss[0], ss[1]).toString();
            } else {
                logger.info("parse i18n file failed: locale is error \"" + localeSrc + "\"");
                return;
            }
            String locale = tempLocale;
            errcodes.forEach(errorcode -> {
                Map<String, String> errorConfig = (Map<String, String>) errorcode;
                Integer code = Integer.valueOf(errorConfig.get("code"));
                String level = errorConfig.get("level");
                String label = errorConfig.get("label");

                ErrorItemImpl errorItem = errorItems.get(Integer.valueOf(code));
                if (errorItem == null) {
                    errorItem = new ErrorItemImpl();
                    errorItem.errorCode = code.intValue();
                    errorItem.level = ErrorCodeLevelUtil.transfer2Int(level);
                    errorItems.put(code, errorItem);
                }
                errorItem.addLabel(locale, label);
            });
        });

        errorItems.forEach((code, errorItem) -> {
            errorItem.unmodifiable();
        });
        this.errorItems = Collections.unmodifiableMap(errorItems);
    }


    static DefaultErrorCodeI18n getInstance() {
        if (singleton == null) {
            lock.lock();
            try {
                if (singleton == null) {
                    singleton = new DefaultErrorCodeI18n();
                }
            } finally {
                lock.unlock();
            }
        }
        return singleton;
    }


    /*
     * (non-Javadoc)
     *
     * @see com.zte.ums.zenap.i18n.ErrorCodeI18n#getErrorItem(int)
     */
    @Override
    public Optional<ErrorItem> getErrorItem(int errorCode) {
        return Optional.ofNullable(errorItems.get(Integer.valueOf(errorCode)));
    }


    public static class ErrorItemImpl implements ErrorItem {

        private int errorCode;

        private int level;

        private Map<String, String> labels = new HashMap<String, String>();

        private String jsonString = null;

        @Override
        public int getErrorCode() {
            return errorCode;
        }

        @Override
        public int getLevel() {
            return level;
        }

        public Map<String, String> getLabels() {
            return labels;
        }

        private void unmodifiable() {
            if (labels != null) {
                labels = Collections.unmodifiableMap(labels);
            }
        }

        private synchronized void addLabel(String locale, String label) {
            labels.put(locale, label);
        }

        @Override
        public String getLabel(Locale theLocale) {
            if (theLocale == null) {
                return null;
            }
            return labels.get(I18nLocaleTransfer.transfer(theLocale, labels.keySet()));
        }

        @Override
        public String getCanonicalLabels(int errorCode) {
            String jsonString = this.jsonString;
            if (jsonString == null) {
                ErrorItem2 errorItem2 = new ErrorItem2();
                errorItem2.setErrorCode(this.errorCode);
                errorItem2.setLevel(ErrorCodeLevelUtil.transfer2String(this.errorCode));
                errorItem2.setErrlabels(labels);
                try {
                    jsonString = I18nJsonUtil.getInstance().writeToJson(errorItem2);
                } catch (Exception e) {
                    logger.info("getCanonicalLabels failed from with param errorCode " + errorCode
                            + " and this errorCode " + this.errorCode, e);
                    return null;
                }
                this.jsonString = jsonString;
            }
            return jsonString;
        }

    }

    protected static class ErrorItem2 {

        private int errorCode;

        private String level;

        private Map<String, String> errlabels;

        public ErrorItem2() {

        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Map<String, String> getErrlabels() {
            return errlabels;
        }

        public void setErrlabels(Map<String, String> errlabels) {
            this.errlabels = errlabels;
        }
    }

    protected static class ErrorCodeLevelUtil {

        public static final int ERROR_LEVEL = javax.swing.JOptionPane.ERROR_MESSAGE;

        public static final int WARN_LEVEL = javax.swing.JOptionPane.WARNING_MESSAGE;

        public static final int INFO_LEVEL = javax.swing.JOptionPane.INFORMATION_MESSAGE;

        protected static String transfer2String(int errorCode) {
            switch (errorCode) {
                case ERROR_LEVEL:
                    return "ERROR";
                case INFO_LEVEL:
                    return "INFO";
                case WARN_LEVEL:
                    return "WARN";
            }
            return null;
        }

        protected static int transfer2Int(String level) {
            switch (level) {
                case "ERROR":
                    return ERROR_LEVEL;
                case "INFO":
                    return INFO_LEVEL;
                case "WARN":
                    return WARN_LEVEL;
            }
            return -1;
        }

    }

}
