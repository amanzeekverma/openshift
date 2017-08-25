package com.zeek.donna.service;

import com.zeek.donna.fwk.ZDBConnection;

import java.sql.ResultSet;

public class DonnaService {
        private static DonnaService _instance = null;

        private DonnaService(){
	}

        public static DonnaService getInstance() {
		if (_instance == null) {
			_instance = new DonnaService();
		}
		return _instance;
	} 

       public boolean checkAccess(int access_code){
                boolean response = false;
                ZDBConnection con = ZDBConnection.getInstance();
	        String query = "SELECT * FROM DONNA_INON_ACCESS WHERE ACCESS_CODE="+access_code;
                try {
		   ResultSet rs = con.executeQuery(query);
		   if (rs.next()){
			response = true;
		   }
		
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		return response;
       }
}
