
package org.openo.baseservice.bus.util;

import java.io.File;

import javax.servlet.ServletContextEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

public class TestRegisterServiceListener {
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testRegisterServiceListener() {
        
        RegisterServiceListener impl = new RegisterServiceListener();
        
        ServletContextEvent sce = null;
        
        new MockUp<File>() {
            @Mock
            public File[] listFiles() {
                File file = new File("");
                File[] filelist = new File[]{file};
                return filelist;
            }
            
        };
        
        impl.contextInitialized(sce);
    }
    
}
