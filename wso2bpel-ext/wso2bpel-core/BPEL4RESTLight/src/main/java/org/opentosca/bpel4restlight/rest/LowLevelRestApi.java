/**
 * This static-class eases HTTP-method execution by self-managed fault-handling
 * and automated Response-information processing
 */
package org.opentosca.bpel4restlight.rest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class LowLevelRestApi {
	
  protected static final Log log = LogFactory.getLog(LowLevelRestApi.class);
	// Local HttpClient used for every communication (Singleton implementation)
//	private static HttpClient httpClient = new HttpClient();
	
	/**
	 * Executes a passed HttpMethod (Method type is either PUT, POST, GET or
	 * DELETE) and returns a HttpResponseMessage
	 * 
	 * @param method Method to execute
	 * @return HttpResponseMessage which contains all information about the
	 *         execution
	 */
	public static HttpResponseMessage executeHttpMethod(HttpMethod method) {
		
		HttpResponseMessage responseMessage = null;
		
		try {
			log.debug("Method invocation on URI: \n");
			log.debug(method.getURI().toString());
			
			// Execute Request
			HttpClient httpClient = new HttpClient();
			httpClient.executeMethod(method);
			responseMessage = LowLevelRestApi.extractResponseInformation(method);
			
		} catch (Exception e) {
		  log.error("call rest error:", e);
		} finally {
			// Release Connection anyway
			method.releaseConnection();
		}
		
		// Extract response information and return
		return responseMessage;
	}
	
	/**
	 * Extracts the response information from an executed HttpMethod
	 * 
	 * @param method Executed Method
	 * @return Packaged response information
	 */
	private static HttpResponseMessage extractResponseInformation(HttpMethod method) {
		// Create and return HttpResponseMethod
		HttpResponseMessage responseMessage = new HttpResponseMessage();
		responseMessage.setStatusCode(method.getStatusCode());
		try {
			responseMessage.setResponseBody(getResponseBody(method));
		} catch (Exception e) {
		    log.error(e);
		}
		return responseMessage;
		
	}
	
	/**
	 * getResponseBody 
	 * 
	 * get response body info, if response body is a json object, then translate json object to xml
	 * if the rest request failed, i.e. the response body is a 404 error page, then response the body with header <root>
	 * @param method
	 * @return
	 * @throws ParseException
	 */
	private static String getResponseBody(HttpMethod method) throws ParseException
	{
	  String result = null;
	  try {
	    result = method.getResponseBodyAsString();
		log.debug("result:");
		log.debug(result);
	  } catch (IOException e) {
	    log.error(e);
	  }
	  
	  Header header = method.getRequestHeader("Accept");
	  if ("application/json".equals(header.getValue())) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
	    sb.append("<root>");
	    if(result != null && !"".equals(result)) {
	      /**
		  if(result.startsWith("<html>")) {
	        sb.append("<![CDATA[");
	        sb.append(result);
	        sb.append("]]>");
	      } else {
	        Object json = new JSONParser().parse(result);
	        json2Xml(sb, "obj", json);
	      }
		  */
		  
		  try {
			Object json = new JSONParser().parse(result);
	        json2Xml(sb, "obj", json);
		  } catch (Exception e) {
			log.error(e);
			sb.append("<![CDATA[");
	        sb.append(result);
	        sb.append("]]>");
		  }
	    }
	    sb.append("</root>");
	    
		log.debug("responseBody:");
		log.debug(sb.toString());
	    return sb.toString();
	  }
	  return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public static void json2Xml(StringBuilder sb, String key,  Object jsonObject) {
	  if(jsonObject == null) {
	    sb.append("<error>empty</error>");
	    return;
	  }
	  
	  if(jsonObject instanceof JSONArray) {
	    JSONArray array = (JSONArray) jsonObject;
	    sb.append("<").append(key).append("s").append(">");
	    for(int i=0, len=array.size(); i<len; i++) {
	      json2Xml(sb, key, array.get(i));
	    }
	    sb.append("</").append(key).append("s").append(">");
	    
	    return;
	  } else if(jsonObject instanceof JSONObject) {
	    sb.append("<").append(key).append(">");
	    JSONObject json = (JSONObject) jsonObject;
	    for(Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>)json.entrySet()) {
	      json2Xml(sb, entry.getKey(), entry.getValue());
	    }
	    sb.append("</").append(key).append(">");
	    return;
	  } else {
	    sb.append("<").append(key).append(">");
	    sb.append("<![CDATA[");
	    sb.append(jsonObject.toString());
	    sb.append("]]>");
	    sb.append("</").append(key).append(">");
	    
	    return;
	  }
	}
	
}
