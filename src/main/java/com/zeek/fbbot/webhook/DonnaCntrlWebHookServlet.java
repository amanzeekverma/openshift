package com.zeek.fbbot.webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.zeek.fbbot.pojo.Attachment;
import com.zeek.fbbot.pojo.FBMessage;
import com.zeek.fbbot.pojo.FBProfile;
import com.zeek.fbbot.pojo.Message;
import com.zeek.fbbot.pojo.Messaging;
import com.zeek.fbbot.pojo.Recipient;
import com.zeek.fbbot.pojo.btnmenu.Button;
import com.zeek.fbbot.pojo.btnmenu.FBButtonMenu;
import com.zeek.fbbot.pojo.btnmenu.Payload;
import com.zeek.services.ZeeKServices;
import com.zeek.utube.pojo.Id;
import com.zeek.utube.pojo.Item;
import com.zeek.utube.pojo.YoutubeRequest;

public class DonnaCntrlWebHookServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

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


          /**************************************/
			//Replace XXXX with right search string.
           private static final String youtubeURL = "https://www.googleapis.com/youtube/v3/search?part=id&q=XXXX&type=video&key=AIzaSyAs4apfcZCcf63CGULKnRb9Y_TSj5KkYD4"; 
           private static final String fbProfileFetchURL = "https://graph.facebook.com/v2.7/XXXX?access_token=EAAHrmIZCBez0BAO6YNNH9LaHMy4liOLGf0pyBGNHCIRPUSSyX8R0Urv73ZCgZBsaDgZBfkueH4SEf73pggQJSuqjdwGf3dp1Sbk0oD8ZCcBSlN8WOy0eJtZCav1T0KX5aNwZCd50PUfCU4HwKo32GXY0nvKnwEFWLV2365GXyO88wZDZD"; 
          /***************************************/

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * get method is used by fb messenger to verify the webhook
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String queryString = request.getQueryString();
		String msg = "Come on, you cannot hit this !";

		if (queryString != null) {
			String verifyToken = request.getParameter("hub.verify_token");
			String challenge = request.getParameter("hub.challenge");
			// String mode = request.getParameter("hub.mode");
			msg=challenge;
			/*if (StringUtils.equals(VERIFY_TOKEN, verifyToken)
					&& !StringUtils.isEmpty(challenge)) {
				msg = challenge;
			} else {
				msg = "";
			}*/
		} else {
			System.out
					.println("Exception no verify token found in querystring:"
							+ queryString);
		}

		response.getWriter().write(msg);
		response.getWriter().flush();
		response.getWriter().close();
		response.setStatus(HttpServletResponse.SC_OK);
		System.out.println("Good Job");
		return;
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	System.out.println("POST REQUEST RECEIVED");
	String responseTxt = "received: ";
	StringBuffer jb = new StringBuffer();
	  String line = null;
	  try {
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	      jb.append(line);
	  } catch (Exception e) { /*report an error*/ }

	  
	  FBMessage fbReq = new Gson().fromJson(jb.toString(), FBMessage.class);
	  System.out.println(jb.toString()); //REMOVE LATER
	  if (fbReq == null){
		  System.out.println("empty FB request");
		  response.setStatus(HttpServletResponse.SC_OK);
		  return;
	  }
	  
	  int entrySize = fbReq.getEntry().size();
	  System.out.println("entry size = "+entrySize);
	  for (int i=0; i< entrySize; i++){
		  List<Messaging> messagings = fbReq.getEntry().get(i)
					.getMessaging();
			for (Messaging messaging : messagings) {
				String sender = messaging.getSender().getId();
				if (messaging.getMessage() != null
						&& messaging.getMessage().getText() != null) {
					String text = messaging.getMessage().getText();
					responseTxt+=text +" ...yay its a living bot!";
                                        if (!findService(text, sender) ){
					   System.out.println("Going to send response = "+responseTxt);
					   sendResponse(sender, responseTxt);
                                        } 
				} else {
					System.out.println("Found a request without message..skipping it   >>>> ");
				}
			}  
	  }
	  System.out.println("Sending HTTP 200 against POST");
	  response.setStatus(HttpServletResponse.SC_OK);
	}

	private void sendResponse(String sender, String responseTxt) {
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

        private boolean findService(String text, final String sender){
           boolean ret = false;
           final String text2 = text.toLowerCase();
           if (text2.startsWith("utube") || text2.startsWith("youtube") || text2.startsWith("video")) {
              ret = true;
              //Async
              System.out.println("Sending Async Youtube req");
              new Thread("utube-th"){
            	  public void run(){
            		  sendYoutubeSearch(text2, sender);
            	  }
              }.start();
              System.out.println("Youtube Async PASS");
           }

            if (text2.startsWith("profile")) {
               ret = true;
               //Async
               System.out.println("Sending Async FB Profile req");
               new Thread("fbprofile-th"){
             	  public void run(){
             		 FBProfile profile = requestFBProfile(sender);
             	  }
               }.start();
               System.out.println("FB Profile Async PASS");
            }
            if (text2.startsWith("speak")){
               ret = true;
               //Async
               System.out.println("Sending Async SPEAK msg");
               new Thread("zeektts-th"){
                  public void run(){
                        try{
                        String action = text2.substring(6);
                        String speakURL = "http://java-codeperformance.rhcloud.com/LaunchModule?target=speak&action="+URLEncoder.encode(action, "UTF-8");
                        System.out.println("sending "+speakURL);
                        URL obj = new URL(speakURL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			}
			in.close();
			}catch(Exception e){
                                System.out.println(e.getMessage());
 				e.printStackTrace();
                        }	
                        sendResponse(sender, "action has been submitted");

                  }
               }.start();
               System.out.println("Async SPEAK sent!");
            } 
            if (text2.startsWith("hi")){ //HURRAY SOME ONE CAME IN!!!!
            	ret = true;
            	new Thread("knock-knock-thread"){
               	  public void run(){
               		 FBProfile profile = requestFBProfile(sender);
               		 boolean isExistingUser = ZeeKServices.getInstance().bot_checkExistingUser(sender);
               		 if (isExistingUser){
               			int user_id = ZeeKServices.getInstance().bot_getUserId(sender);
               			String reponseTxt = "Welcome back! : "+user_id;
                    	sendResponse(sender, reponseTxt);
               		 } else {
               			int user_id = ZeeKServices.getInstance().bot_addNewUser(profile, sender);
               			String reponseTxt = "Thank you for using Donna! Ur USER-ID = "+user_id;
                    	sendResponse(sender, reponseTxt);
               		 }
               		 
               		 //SENDING MENU
               		 List<String> _btnList = new ArrayList<String>();
               		 _btnList.add("ADD");
               		 _btnList.add("VIEW");
               		 _btnList.add("DELETE");
               		 sendButtonMenu(sender, _btnList);
               		 
               	  }
                 }.start();
            }

           return ret; 
        }

        
        
        private void sendButtonMenu(String sender, List<String> button){
        	Recipient recipient = new Recipient();
    		recipient.setId(sender);
    		
    		com.zeek.fbbot.pojo.btnmenu.Message msg = new com.zeek.fbbot.pojo.btnmenu.Message();
    		com.zeek.fbbot.pojo.btnmenu.Attachment attachment = new com.zeek.fbbot.pojo.btnmenu.Attachment();
    		attachment.setType("template");
    		Payload payload = new Payload();
    		payload.setTemplateType("button");
    		payload.setText("Please select one of the following:");
    		
    		Button addBtn = new Button();
    		addBtn.setTitle("ADD");
    		addBtn.setType("postback");
    		addBtn.setPayload("youtube hurray");
    		
    		Button viewBtn = new Button();
    		viewBtn.setTitle("VIEW");
    		viewBtn.setType("postback");
    		viewBtn.setPayload("youtube yahoo");
    		
    		List<Button> buttons = new ArrayList<Button>();
    		buttons.add(addBtn);
    		buttons.add(viewBtn);
    		
    		payload.setButtons(buttons);
    		attachment.setPayload(payload);
    		
                msg.setAttachment(attachment);
 
    		FBButtonMenu  respMenu = new FBButtonMenu();
    		respMenu.setMessage(msg);
    		respMenu.setRecipient(recipient);
    		
    		String jsonReply = new Gson().toJson(respMenu);
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
        
        
        private FBProfile requestFBProfile(String sender){
        	//retrieve FB profile from FB
        	String url = fbProfileFetchURL.replace("XXXX", sender);
        	HttpGet http_getFBProfile = new HttpGet(url);
        	System.out.println("Request TO FB = "+url);
        	FBProfile profile = null;
        	try {
        	HttpResponse ack = client.execute(http_getFBProfile);
			String result = EntityUtils.toString(ack.getEntity());
			System.out.println("Response from FB = "+result);
			profile = new Gson().fromJson(result, FBProfile.class );
        	}catch(Exception e){
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    		}
        	String reponseTxt = "Hi "+profile.getFirstName()+"!";
        	sendResponse(sender, reponseTxt);
        	return profile;
        } 

        private void sendYoutubeSearch(String text, String sender){
        	//retreive from youtube
                text = text.split(" ",2)[1];
        	String url = youtubeURL.replace("XXXX", text);
        	url = url.replaceAll(" ", "%20");
        	HttpGet http_utubeGet = new HttpGet(url);
        	System.out.println("Request TO youtube = "+url);
        	YoutubeRequest utubeReq  = null;
        	try {
        	HttpResponse ack = client.execute(http_utubeGet);
			String result = EntityUtils.toString(ack.getEntity());
			System.out.println("Response from Youtube = "+result);
			utubeReq = new Gson().fromJson(result, YoutubeRequest.class );
        	}catch(Exception e){
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    		}
			
			
			List<Item> _listVids = utubeReq.getItems();
			if (_listVids.size() <= 0){
				Item fake = new Item();
				Id id = new Id();
				id.setVideoId("VztSdwYPFCE");
				fake.setId(id);
			}
			String urlRes = _listVids.get(0).getId().getVideoId();
			System.out.println("Extracted id = "+urlRes);
                String utubeURL = "https://www.youtube.com/watch?v="+urlRes; 
        	//send to chat
                sendResponse(sender, utubeURL); 
        	/*  UNABLE TO SEND THE VIDEO VIDEO
                Recipient recipient = new Recipient();
    		recipient.setId(sender);
    		Message fbRes = new Message();
    		Attachment vid = new Attachment();
    		Payload payload = new Payload();
    		vid.setType("video");
                String utubeURL = "https://www.youtube.com/watch?v="+urlRes;
    		payload.setUrl(utubeURL);
    		vid.setPayload(payload);
    		List<Attachment> listVids = new ArrayList<Attachment>();
    		listVids.add(vid);
    		fbRes.setAttachments(listVids);
    		Messaging reply = new Messaging();
    		reply.setRecipient(recipient);
    		reply.setMessage(fbRes);
    		String jsonReply = new Gson().toJson(reply);
    		System.out.println("JSON = "+jsonReply);
    		try {
    			HttpEntity entity  = new ByteArrayEntity(jsonReply.getBytes("UTF-8"));
    			httppost.setHeader("Content-Type", "application/json");
    			httppost.setEntity(entity);
    			HttpResponse ack2 = client.execute(httppost);
    			String result2 = EntityUtils.toString(ack2.getEntity());
    			System.out.println(result2);
    			
    		}catch(Exception e){
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    		}*/
        }
}

