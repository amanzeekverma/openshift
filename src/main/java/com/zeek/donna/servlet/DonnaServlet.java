package com.zeek.donna.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zeek.donna.service.DonnaService;

public class DonnaServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.write("I am working fine");
                out.flush();
		out.close();
                response.setStatus(HttpServletResponse.SC_OK);
	        return;	
	}

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                  response.setContentType("text/html");  
    		  PrintWriter out = response.getWriter();   
                  String access_code = request.getParameter("access_code");
                  DonnaService _service = DonnaService.getInstance();
                  int ac = Integer.parseInt(access_code);
	          boolean allowed =  _service.checkAccess(ac);
                  if (allowed) {
			out.println("OK");
		  }   else {
			out.println("BAD");
		  }
		  out.flush();
                  out.close();
		  response.setStatus(HttpServletResponse.SC_OK);
        }

}
