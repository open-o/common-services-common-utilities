
package org.openo.baseservice.bus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.util.impl.SystemEnvVariablesDefImpl;

import junit.framework.Assert;
import mockit.Mock;
import mockit.MockUp;

public class TestRegisterService {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testregisterServce() throws IOException {
        
        File file = new File("");
        Response res = null;
        
        final String path = file.getAbsolutePath();
        
        new MockUp<SystemEnvVariablesDefImpl>() {
            @Mock
            public String getAppRoot() {
                return path;
            }
        };
        
        try {
            res = RegisterService.registerServce(path, true);
        } catch(Exception e) {
            Assert.assertNotNull(e);
        }
        
    }
}

