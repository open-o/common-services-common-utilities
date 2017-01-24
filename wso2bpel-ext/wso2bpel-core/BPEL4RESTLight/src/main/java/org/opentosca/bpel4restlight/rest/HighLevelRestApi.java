/**
 * This class wraps HTTP-Method functionality and thereby abstracts from low
 * level code to simplify the usage.
 */
package org.opentosca.bpel4restlight.rest;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;


public class HighLevelRestApi {
  protected static final Log log = LogFactory.getLog(HighLevelRestApi.class);
	/**
	 * This method implements the HTTP Put Method
	 * 
	 * @param uri
	 *            Resource URI
	 * @param requestPayload
	 *            Content which has to be put into the Resource
	 * @return ResponseCode of HTTP Interaction
	 */
	@SuppressWarnings("deprecation")
	public static HttpResponseMessage Put(String uri, String requestPayload, String acceptHeaderValue, String contentTypeHeader) {

		PutMethod method = new PutMethod(uri);
		// requestPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		// requestPayload;

		HighLevelRestApi.setHeader(method, acceptHeaderValue, contentTypeHeader);
		method.setRequestBody(requestPayload);

		HttpResponseMessage responseMessage = LowLevelRestApi.executeHttpMethod(method);

		// kill <?xml... in front of response
		HighLevelRestApi.cleanResponseBody(responseMessage);

		return responseMessage;
	}

	/**
	 * This method implements the HTTP Post Method
	 * 
	 * @param uri
	 *            Resource URI
	 * @param requestPayload
	 *            Content which has to be posted into the Resource
	 * @return ResponseCode of HTTP Interaction
	 */
	@SuppressWarnings("deprecation")
	public static HttpResponseMessage Post(String uri, String requestPayload, String acceptHeaderValue, String contentTypeHeader) {

		PostMethod method = null;
		if (uri.contains("?")) {
			log.debug("Found query trying to split");
			String[] split = uri.split("\\?");
			log.debug("Raw URI part: " + split[0]);
			log.debug("Raw Query part: " + split[1]);
			method = new PostMethod(split[0]);
			method.setQueryString(HighLevelRestApi.createNameValuePairArrayFromQuery(split[1]));
		} else {
			method = new PostMethod(uri);
			;
		}
		method.setRequestBody(requestPayload);
		HighLevelRestApi.setHeader(method, acceptHeaderValue, contentTypeHeader);
		HttpResponseMessage responseMessage = LowLevelRestApi.executeHttpMethod(method);
		HighLevelRestApi.cleanResponseBody(responseMessage);
		return responseMessage;
	}

	/**
	 * This method implements the HTTP Get Method
	 * 
	 * @param uri
	 *            Resource URI
	 * @return Content represented by the Resource URI
	 */
	public static HttpResponseMessage Get(String uri, String acceptHeaderValue, String contentTypeHeader) {
		log.debug("Setting URI to: \n");
		log.debug(uri);
		GetMethod method = null;
		if (uri.contains("?")) {
		  log.debug("Found query trying to split");
			String[] split = uri.split("\\?");
			log.debug("Raw URI part: " + split[0]);
			log.debug("Raw Query part: " + split[1]);
			
			method = new GetMethod(split[0]);
			method.setQueryString(HighLevelRestApi.createNameValuePairArrayFromQuery(split[1]));
		} else {
			method = new GetMethod(uri);
		}
		HighLevelRestApi.setHeader(method, acceptHeaderValue, contentTypeHeader);
		HttpResponseMessage responseMessage = LowLevelRestApi.executeHttpMethod(method);
		HighLevelRestApi.cleanResponseBody(responseMessage);
		return responseMessage;
	}

	private static NameValuePair[] createNameValuePairArrayFromQuery(String query) {
		// example:
		// csarID=Moodle.csar&serviceTemplateID={http://www.example.com/tosca/ServiceTemplates/Moodle}Moodle&nodeTemplateID={http://www.example.com/tosca/ServiceTemplates/Moodle}VmApache
		log.debug("Splitting query: " + query);
		String[] pairs = query.trim().split("&");
		NameValuePair[] nameValuePairArray = new NameValuePair[pairs.length];
		int count = 0;
		for (String pair : pairs) {
		  log.debug("Splitting query pair: " + pair);
			String[] keyValue = pair.split("=");
			NameValuePair nameValuePair = new NameValuePair();
			log.debug("Key: " + keyValue[0] + " Value: " + keyValue[1]);
			nameValuePair.setName(keyValue[0]);
			nameValuePair.setValue(keyValue[1]);
			nameValuePairArray[count] = nameValuePair;
			count++;
		}
		return nameValuePairArray;
	}

	/**
	 * This method implements the HTTP Delete Method
	 * 
	 * @param uri
	 *            Resource URI
	 * @return ResponseCode of HTTP Interaction
	 */
	public static HttpResponseMessage Delete(String uri, String acceptHeaderValue, String contentTypeHeader) {

		DeleteMethod method = new DeleteMethod(uri);
		HighLevelRestApi.setHeader(method, acceptHeaderValue, contentTypeHeader);
		HttpResponseMessage responseMessage = LowLevelRestApi.executeHttpMethod(method);
		HighLevelRestApi.cleanResponseBody(responseMessage);
		return responseMessage;
	}

	private static void setHeader(HttpMethodBase method, String accept, String contentType) {
		if (!"".equals(accept)) {
			method.setRequestHeader(HttpHeaders.ACCEPT, accept);
		} else {
			method.setRequestHeader(HttpHeaders.ACCEPT, "application/xml");
		}
		
		if (contentType != null && !"".equals(contentType)) {
		  method.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
        } else {
//          method.setRequestHeader("Accept", accept);
        }
		
	}

	private static void cleanResponseBody(HttpResponseMessage responseMessage) {
	  log.debug("ResponseBody: \n");
	  if (responseMessage != null && responseMessage.getResponseBody() != null) {  
	    log.debug(responseMessage.getResponseBody());
	    String temp = responseMessage.getResponseBody()
	        .replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
	    responseMessage.setResponseBody(temp);
	  }
	}
	
}
