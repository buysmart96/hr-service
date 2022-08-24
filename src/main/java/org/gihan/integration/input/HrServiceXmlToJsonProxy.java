package org.gihan.integration.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codeobe.integration.CodeobeListener;
import org.codeobe.integration.CodeobeLog;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


//ProxyServiceRestController
//HRProxyService
@RestController
@RequestMapping("/hrservice-proxy")
//@EnableIntegration
public class HrServiceXmlToJsonProxy extends CodeobeListener {
	

	@Autowired
	CodeobeLog codeobeLog;
	
	@Value("${output.http_endpoint}")
	String outputEndpoint;
	
	String peid;

	@GetMapping("/test")
	public String test(){
		System.out.printf("\n\nTest func called");
		return "Hello Test";
	}

	@GetMapping("/hello/{user}")
	public String healthCheck(@PathVariable String user) {
		System.out.println("Hello " + user);
		return "Hello " +  user + " " +new Date().toString();
	}


	@PostMapping(value = "/add-employee")
	public List<String> addEmployee(@RequestBody String request)
	{
		System.out.println("\n1).XML Request received @REST interface request= " + request);
		peid = UUID.randomUUID().toString();

		try{
			codeobeLog.logMessageBeforeProcess(request,peid);
		}catch (Exception e){
			System.out.println("\nLogging Error : Log Message Before Error..\n\n");
		}

//		System.out.println("\n\nIm here");
		List<String> responseList = processAndSend(request,peid);

		return responseList;
	}


	@Override
	public List<String> processAndSend(String msg, String peid) {

		System.out.println("2. Start processing request =" + msg);

		List<String> employeeArray = convertXmlToJson(msg);
		List<String> processedList = new ArrayList<String>();
		
		if (employeeArray != null) {
			for (String employee : employeeArray) {
				System.out.println("2.1 Processing employee details =" + employee);


				try{
					codeobeLog.logMessageAfterProcess(employee, peid); //send to Q
				}catch (Exception e){
					System.out.println("\nLogging Error: Log Message After Process..\n\n");
				}

				processedList.add(employee);
			}
		} else {
			System.out.println("2.2 Processing employee error=" + msg);

			try{
				codeobeLog.logProcessingError("Invalid Request", peid); //send to q
			}catch (Exception e){
				System.out.println("\nLogging Error: Log Error After Process..\n\n");
			}

			processedList.add("Invalid Request");
		}
		
		//Make sure you call send method from process to make it work for resends.replays
		System.out.println("Um in end of the sendAndProcess func");
		List<String> results  = send(processedList, peid);
		return results;
	}
	
	
	@Override
	public List<String> send(List<String> processedList, String peid)  {
		
		List<String> reponseList = new ArrayList<String>();
		for (String msg  : processedList)  {
			 
			
			System.out.println("3. sendToHttpEndpoint ....." + msg);
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(outputEndpoint);
	
		    String tmOut = null;
		    try {
			    StringEntity entity = new StringEntity(msg);
			    httpPost.setEntity(entity);
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");
			  
			    CloseableHttpResponse response = client.execute(httpPost);
			    if (response != null && response.getStatusLine().getStatusCode() == 200) {
			    	tmOut = EntityUtils.toString(response.getEntity());

					try{
						codeobeLog.logResponse(tmOut, peid);
					}catch (Exception e){
						System.out.println("\nLogging Error: Log Response.\n\n");
					}


					System.out.println("3.1 logResponse ....." );
			    } else {
			    	tmOut = "Error from endpoint";

					try{
						codeobeLog.logResponseError(tmOut, peid);
					}catch (Exception e){
						System.out.println("\nLogging Error: Log Response Error.\n\n");
					}


					System.out.println("3.2 logResponseError ....." );
			    }
		    }  catch (Exception ex) {
		    	tmOut = "Error sending message out";

				try{
					codeobeLog.logResponseError(tmOut, peid);
				}catch (Exception e){
					System.out.println("\nLogging Error: Log Response Error.\n\n");
				}

				System.out.println("3.3 logResponseException ....." );
		    } finally {
		    	try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    reponseList.add(tmOut);
		}
	    return reponseList;
	}

	public List<String> convertXmlToJson(String msg){
		List<String> outArray = new ArrayList<String>();
		try{
			JSONObject json = XML.toJSONObject(msg);
//			System.out.println(json);

			String jsonString = json.toString(4);

			String template = "{\"name\":\"%s\", \"age\":\"%s\"}";
			String[] messages = jsonString.split("\n");
//			System.out.println(messages.length);
			if (messages.length > 0)
			{
				for(int i=0; i<messages.length; i++)
				{
					if(i != 0 && i != messages.length -1)
					{
						String sentence;
//						System.out.println(messages[i]);
						if(messages[i].contains("{")) {
							messages[i+1] = messages[i+1].replaceAll("\\s", "");
							messages[i+2] = messages[i+2].replaceAll("\\s", "");


							String[] mParts1 = messages[i+1].split(":");
							String[] mParts2 = messages[i+2].split(":");

							Pattern p = Pattern.compile("\"([^\"]*)\"");
							Matcher m = p.matcher(mParts1[1]);
							String name = "";
							while (m.find()) {
//							  System.out.println(m.group(1));
								name += m.group(1);
							}
//							System.out.println(name);
							outArray.add(String.format(template, name, mParts2[1]));

							i += 2;
						}else if(messages.length == 4) {
//							System.out.println("\n\nmsg leng = 4");
//							System.out.println("message "+ i + messages[i]);
							messages[i] = messages[i].replaceAll("\\s", "");
							messages[i+1] = messages[i+1].replaceAll("\\s", "");

							String[] mParts1 = messages[i].split(":");
							String[] mParts2 = messages[i+1].split(":");

							Pattern p = Pattern.compile("\"([^\"]*)\"");
							Matcher m = p.matcher(mParts1[1]);
							String name = "";
							while (m.find()) {
//							  System.out.println(m.group(1));
								name += m.group(1);
							}

//							System.out.println("name " + name);
//							System.out.println("mPart2 "+ mParts2[1]);

							outArray.add(String.format(template, name, mParts2[1]));

							i += 2;


						}
					}

				}
			}


		}catch(JSONException e){
			System.out.println(e.toString());
			outArray  = null;
		}
		return outArray;
	}

}
