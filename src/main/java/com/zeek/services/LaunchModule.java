package com.zeek.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LaunchModule extends HttpServlet {
	private static final long serialVersionUID = 1L;
        private ArrayList<String> ttsQueue = new ArrayList<String>();
        private static int ard_light = 0;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();

		String req = request.getParameter("target");
		// FALL CHECK!
		String fallCheck = request.getParameter("app");
		if (fallCheck != null && fallCheck.equals("fall")) {
			String addPlaylist = request.getParameter("AddPlaylist");
			if (addPlaylist != null) {
				ZeeKServices.getInstance().addPlaylist(addPlaylist);
				return;
			}
			String addSong = request.getParameter("AddSong");
			String toPlaylist = request.getParameter("p");
			if (addSong != null && toPlaylist != null) {
				ZeeKServices.getInstance().addSong(addSong, toPlaylist);
				return;
			}
		}
		// FALL CHECK ENDS
               //ARDUINO-LIGHT-PROJECT START
 	       if (req.equals("arduino")) { //TARGET = arduino 
                  String action = request.getParameter("action");
                  if (action.equals("pop")) {
 			writer.println(ard_light);
                  }  else if (action.equals("on")){
                        LaunchModule.ard_light=1;
                  }  else if (action.equals("off")){
                        LaunchModule.ard_light=0;
                  } else {
                        writer.println("Please contact Aman Verma");
                  }
                return;         
               }
    	       //ARDUINO LIGHT ENDS
               //TTS CHECK STARTS
               if (req.equals("speak")) { //TARGET = SPEAK
                  String action = request.getParameter("action");
                  if (action.equals("reset")){
                      ttsQueue = null;
                      ttsQueue = new ArrayList<String>();
                  } else if (action.equals("pop")) {
                      if (ttsQueue.size() < 1) 
                            return;
                      writer.println(ttsQueue.get(0));
                      ttsQueue.remove(0); 
                  } else {
                     ttsQueue.add(action); 
                  }
                 return; 
               }
               // TTS CHECK ENDS
		try {
			if (req.equals("GPS1")) { // TARGET = GPS1
				String loc = request.getParameter("Location");
				if (loc != null) {
					ZeeKServices.getInstance().addLocation(loc);
					return;
				}
				String id = request.getParameter("id");
				if (id != null) {
					loc = ZeeKServices.getInstance().getLocationString(
							Integer.parseInt(id));
					writer.println(loc);

				}
			} else {
			//ACTUAL DB BASED DEFER LAUNCH
			String moduleTarget = ZeeKServices.getInstance().getModuleTarget(req);
			writer.println(moduleTarget);
		        }	
		} catch (Exception e) {
			writer.println("ERROR : "+e.getMessage());
                        e.printStackTrace();
		}

	}

}
