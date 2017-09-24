package com.zeek.apiai.webhook;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.zeek.apiai.pojo.ApiaiRequest;
import com.zeek.fbbot.pojo.Message;
import com.zeek.fbbot.pojo.Messaging;
import com.zeek.fbbot.pojo.Recipient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiAiWebHook  extends HttpServlet{

	/************* FB Chat Bot variables *************************/
    public static final String PAGE_TOKEN = "EAAHrmIZCBez0BAO6YNNH9LaHMy4liOLGf0pyBGNHCIRPUSSyX8R0Urv73ZCgZBsaDgZBfkueH4SEf73pggQJSuqjdwGf3dp1Sbk0oD8ZCcBSlN8WOy0eJtZCav1T0KX5aNwZCd50PUfCU4HwKo32GXY0nvKnwEFWLV2365GXyO88wZDZD";
    private static final String VERIFY_TOKEN = "SOMETHING";
    private static final String FB_MSG_URL = "https://graph.facebook.com/v2.6/me/messages?access_token="
                    + PAGE_TOKEN;
    /*************************************************************/

    /******* for making a post call to fb messenger api **********/
    private static final HttpClient client = HttpClientBuilder.create().build();
    private static final HttpPost httppost = new HttpPost(FB_MSG_URL);
    /*************************************************************/

	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L; //12 for Tom Brady!

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//String queryString = request.getQueryString();
		String msg = "Come on, you cannot hit this !... Any ways, just in case you tried to check the server, yeah its up baby!";
		response.getWriter().write(msg);
		response.getWriter().flush();
		response.getWriter().close();
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}



	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("POST REQUEST RECEIVED");
		String responseTxt = "received: ";
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  
		  ApiaiRequest apiai_request = new Gson().fromJson(jb.toString(), ApiaiRequest.class);
		  System.out.println(jb.toString()); //REMOVE LATER
		  
		  if (apiai_request == null){
			  System.out.println("empty API.AI request");
			  response.setStatus(HttpServletResponse.SC_OK);
			  return;
		  }
		  
		  String query = apiai_request.getResult().getResolvedQuery();
                  if (query.contains("lights")){
                         String url = "https://java-zeek.a3c1.starter-us-west-1.openshiftapps.com/LaunchModule?target=arduino&action=";
                         if (query.contains(" on ")){
                               url = url+"on"; 
                         } else {
                               url = url + "off";
                         }
			try {
                         URL obj = new URL(url);
                         HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                         con.setRequestMethod("GET");
                         int responseCode = con.getResponseCode();
                         BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			 in.close();
			}catch(Exception e){} //sorry
                  }
 	          String receiverName = apiai_request.getResult().getParameters().getAmanRecipientList();
		  String sender = "833019983465255";
		  if (receiverName.equals("Aman")){
			  sender = "833019983465255";
		  } else if (receiverName.equals("Anjali")){
			  sender = "580335202091833";
		  } else if (receiverName.equals("Shatik")){
			  sender = "1223948630982828";
		  }
		 // System.out.println("Query = "+query);
		  
		  if (query.toLowerCase().contains("saying")){
			  query = query.toLowerCase(); 
			  int index = query.indexOf("saying") + 7;
			  query = query.substring(index);
		  }
		  query = "MESSAGE FROM AMAN'S GOOGLE HOME: "+query; 
		  sendMessage(sender, query);
		  
		  
	}
	
	private void sendMessage(String sender, String responseTxt) {
        Recipient recipient = new Recipient();
        recipient.setId(sender);
        Messaging reply = new Messaging();
        Message fbRes = new Message();
        fbRes.setText(responseTxt);
        reply.setRecipient(recipient);
        reply.setMessage(fbRes);
        String jsonReply = new Gson().toJson(reply);
        System.out.println("JSON = "+jsonReply);
        try {
                HttpEntity entity  = new ByteArrayEntity(jsonReply.getBytes("UTF-8"));
                httppost.setHeader("Content-Type", "application/json");
                httppost.setEntity(entity);
                HttpResponse ack = client.execute(httppost);
                String result = EntityUtils.toString(ack.getEntity());
                System.out.println(result);

        }catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
        }
}

	
	
	
}
