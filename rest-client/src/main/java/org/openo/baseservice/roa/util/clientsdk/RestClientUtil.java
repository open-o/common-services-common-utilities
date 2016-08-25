/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.roa.util.clientsdk;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * Rest Client Tools.
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 28-May-2016
 */
public class RestClientUtil {

    private static Set<Class<?>> ret = new HashSet<>();

    static {
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
    }

    private RestClientUtil() {

    }

    /**
     * Processing HTTP requests.
     * <br/>
     * 
     * @param method method name.
     * @param path request path.
     * @param parameters parameters.
     * @param restFull ReST request instance
     * @return The ReST response.
     * @throws ServiceException Exception information.
     * @since SDNO 0.5
     */
    public static RestfulResponse invokeMethod(final String method, final String path,
            final RestfulParametes parameters, final Restful restFull) throws ServiceException {
        RestfulResponse response;
        if("get".equalsIgnoreCase(method)) {
            response = restFull.get(path, parameters);
        } else if("put".equalsIgnoreCase(method)) {
            response = restFull.put(path, parameters);
        } else if("post".equalsIgnoreCase(method)) {
            response = restFull.post(path, parameters);
        } else if("delete".equalsIgnoreCase(method)) {
            response = restFull.delete(path, parameters);
        } else if("patch".equalsIgnoreCase(method)) {
            response = restFull.patch(path, parameters);
        } else {
            throw new ServiceException("NotSuppertMethod", 400);
        }
        return response;
    }

    /**
     * An asynchronous HTTP request.
     * <br/>
     * 
     * @param method http method.
     * @param path request path.
     * @param parameters request parameters.
     * @param restFull restFull instance.
     * @param callback callback function.
     * @throws ServiceException in case error.
     * @since SDNO 0.5
     */
    public static void invokeAsyncMethod(final String method, final String path, final RestfulParametes parameters,
            final Restful restFull, final RestfulAsyncCallback callback) throws ServiceException {
        if("get".equalsIgnoreCase(method)) {
            restFull.asyncGet(path, parameters, callback);
        } else if("put".equalsIgnoreCase(method)) {
            restFull.asyncPut(path, parameters, callback);
        } else if("post".equalsIgnoreCase(method)) {
            restFull.asyncPost(path, parameters, callback);
        } else if("delete".equalsIgnoreCase(method)) {
            restFull.asyncDelete(path, parameters, callback);
        } else if("patch".equalsIgnoreCase(method)) {
            restFull.asyncPatch(path, parameters, callback);
        } else {
            throw new ServiceException("NotSuppertMethod", 400);
        }
    }

    /**
     * Determine whether a class is a native.<br/>
     * 
     * @param clazz: class type.
     * @return whether primitive or not.
     * @since SDNO 0.5
     */
    public static boolean isPrimitive(final Class<?> clazz) {
        if(clazz.isPrimitive()) {
            return true;
        }
        if(ret.contains(clazz)) {
            return true;
        }
        return false;
    }
}
