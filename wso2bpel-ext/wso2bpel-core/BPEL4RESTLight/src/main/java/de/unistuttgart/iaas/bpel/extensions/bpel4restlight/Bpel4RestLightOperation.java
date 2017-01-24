package de.unistuttgart.iaas.bpel.extensions.bpel4restlight;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ode.bpel.common.FaultException;
import org.apache.ode.bpel.runtime.extension.AbstractSyncExtensionOperation;
import org.apache.ode.bpel.runtime.extension.ExtensionContext;
import org.opentosca.bpel4restlight.rest.HighLevelRestApi;
import org.opentosca.bpel4restlight.rest.HttpMethod;
import org.opentosca.bpel4restlight.rest.HttpResponseMessage;
/**
 * 
 * Copyright 2011 IAAS University of Stuttgart <br>
 * <br>
 * 
 * This class provides 4 BPEL4RestLight ExtensionActivity-operations which
 * correspond to the 4 typical REST-Operations GET, PUT, POST and Delete.
 * 
 * @author uwe.breitenbuecher@iaas.uni-stuttgart.de
 * 
 */
import org.w3c.dom.Element;

import de.unistuttgart.iaas.bpel.extensions.bpel4restlight.util.Bpel4RestLightUtil;
import de.unistuttgart.iaas.bpel.util.BPELVariableInjectionUtil;
import de.unistuttgart.iaas.bpel.util.BpelUtil;


public class Bpel4RestLightOperation extends AbstractSyncExtensionOperation {
  protected static final Log log = LogFactory.getLog(Bpel4RestLightOperation.class);
  private static final String EXT_PROPERTIES = "wso2bps-ext.properties"; // extra properties setting
  private static final String MSB_URL = "MSB_URL";  // http://msb:port, ext property msb url, if exist replace the request url 
  
  
	public static String wrapper = "temporary-simple-type-wrapper";
	private static String msbUrl = null;
	
	static {
      
      try {
          File file = new File(EXT_PROPERTIES);
          if(file.exists()) {
              Properties p=new Properties();
              InputStream inputStream = new FileInputStream(file);
              p.load(inputStream);
              inputStream.close();
              msbUrl = p.getProperty(MSB_URL);
          }
          
      } catch (Exception e) {
          e.printStackTrace();
      }
      
  }
	
	
	private void processResponseMessage(HttpResponseMessage responseMessage, ExtensionContext context, Element element) throws FaultException {
		// Write responsePayload to designated variable
		String responsePayloadVariableName = Bpel4RestLightUtil.getMethodAttributeValue(element, MethodAttribute.RESPONSEPAYLOADVARIABLE);
		String statusCodeVariableName = Bpel4RestLightUtil.getMethodAttributeValue(element, MethodAttribute.STATUSCODEVARIABLE);
		
		if (responsePayloadVariableName != null && !responsePayloadVariableName.equals("")) {
			BpelUtil.writeResponsePayloadToVariable(context, responseMessage.getResponseBody(), responsePayloadVariableName, Bpel4RestLightOperation.wrapper);
		}
		
		if (statusCodeVariableName != null && !statusCodeVariableName.equals("")) {
			String StatusCode = "" + responseMessage.getStatusCode();  // int can not be casted to String
			
			BpelUtil.writeResponsePayloadToVariable(context, StatusCode, statusCodeVariableName, Bpel4RestLightOperation.wrapper);
			
		}
	}
	
	/** {@inheritDoc} */
	@Override
	protected void runSync(ExtensionContext context, Element element) throws FaultException {
		element = BPELVariableInjectionUtil.replaceExtensionVariables(context, element);
		log.debug("LocalName of edited element: " + element.getLocalName());
		String httpMethod = element.getLocalName();
		
		// Extract requestUri
		String requestUri = getRequestUrl(element);
		
		HttpResponseMessage responseMessage = null;
		String acceptHeader = Bpel4RestLightUtil.extractAcceptHeader(context, element);
		String contentTypeHeader = Bpel4RestLightUtil.extractContentTypeHeader(context, element);
		String requestPayload = Bpel4RestLightUtil.extractRequestPayload(context, element);
		
		// Execute corresponding HttpMethod via the HighLevelRestApi
		switch (HttpMethod.valueOf(httpMethod)) {
		
			case PUT: {
				responseMessage = HighLevelRestApi.Put(requestUri, requestPayload, acceptHeader, contentTypeHeader);
				break;
			}

			case POST: {
				responseMessage = HighLevelRestApi.Post(requestUri, requestPayload, acceptHeader, contentTypeHeader);
				break;
			}
			
			case GET: {
				responseMessage = HighLevelRestApi.Get(requestUri, acceptHeader, contentTypeHeader);
				break;
			}
			
			case DELETE: {
				responseMessage = HighLevelRestApi.Delete(requestUri, acceptHeader, contentTypeHeader);
				break;
			}
		}
		
		processResponseMessage(responseMessage, context, element);
		// Bpel4RestLightOperation.wrapper = null;
	}
	
	
	/**
	 * getRequestUrl
	 * get request url from element, if exists msbUrl property, then replace the ip and port
	 * @param element
	 * @return
	 */
	private String getRequestUrl(Element element) {
	  String requestUri = Bpel4RestLightUtil.getMethodAttributeValue(element, MethodAttribute.REQUESTURI);
	  log.debug("original url:" + requestUri);
      if(msbUrl == null || "".equals(msbUrl) || "http://msb:port".equals(msbUrl)) {
          return requestUri;
      } else {
          requestUri = requestUri.substring(requestUri.indexOf("//") + 2);
          int index = requestUri.indexOf("/");
          if(index == -1) {
              
          } else {
              requestUri = requestUri.substring(index);
          }
          requestUri = msbUrl + requestUri;
          log.debug("changed url:" + requestUri);
          return requestUri;
      }
  }
}
