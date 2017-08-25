package com.zeek.services;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zeek.fbbot.pojo.FBProfile;
import com.zeek.fwk.ZDBConnection;

public class ZeeKServices {
	private static ZeeKServices _instance = null;
	private static HashMap<String, List<String>> _listMap = new HashMap<String, List<String>>();
	private static HashMap<String, String> _queryMap = new HashMap<String, String>();

	
	//BOT QUERIES
        private static final String insertBotFbUser_template = "INSERT INTO BOT_FB_USER(USER_ID,FB_USER_ID,FNAME,LNAME,GENDER,LOCALE,TIMEZONE,PIC) VALUES (NULL,'FB_SENDER','FB_FNAME','FB_LNAME','FB_GENDER','FB_LOCALE','FB_TZ','FB_PIC')";	
	
	//forcing singleton
	private ZeeKServices() {
		
		//adding basic queries here.. quick solution for now
		_queryMap.put("LIST", "SELECT LIST_NAME FROM LIST");
		_queryMap.put("PLAYLIST", "SELECT PLAYLIST FROM PLAYLIST");
        _queryMap.put("MODULES", "SELECT MODULE_NAME, ACCESS_CODE FROM MODULES");
	}

	public static ZeeKServices getInstance() {
		if (_instance == null) {
			_instance = new ZeeKServices();
		}
		return _instance;
	}
	
	public List<String> fetchList(String item){
		//List<String> _list = _listMap.get(item);
		/*if (_list != null){
			return _list;
		}*/
		ArrayList<String> list = new ArrayList<String>();
		ZDBConnection con = ZDBConnection.getInstance();
		String query = _queryMap.get(item);
		try {
		ResultSet rs = con.executeQuery(query);
		while (rs.next()){
			list.add(rs.getString(1));
		}
		
		//_listMap.put(item, list);
		}catch(Exception e){
			list.add("error");
			e.printStackTrace();
		}
		con.close();
		return list;
		
	}

        public HashMap<String, Integer> fetchModule(String item){
               HashMap<String, Integer> _modules = new HashMap<String, Integer>();
               ZDBConnection con = ZDBConnection.getInstance();
               String query = _queryMap.get(item);
               try {
                ResultSet rs = con.executeQuery(query);
                while (rs.next()){
                    _modules.put(rs.getString(1),rs.getInt(2));
                }
                }catch(Exception e){
                    _modules.put("error",0);
                    e.printStackTrace();
                }
                con.close();
                return _modules;
        }
	
	
	public HashMap<String, List> fetchSongs(){
		HashMap<String, List> _map = new HashMap<String, List>();
		ArrayList<String> list = new ArrayList<String>();
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "SELECT PLAYLIST, SONG FROM SONGS";
		try {
		ResultSet rs = con.executeQuery(query);
		while (rs.next()){
			String playlist = rs.getString(1);
			String song = rs.getString(2);
			List l = _map.get(playlist);
			if (l == null){
				l = new ArrayList();
				_map.put(playlist, l);
			}
			l.add(song);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		return _map;
		
	}

	public void addPlaylist(String addPlaylist) {
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "INSERT INTO PLAYLIST (PLAYLIST) VALUES (\""+addPlaylist+"\")";
		try {
		con.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
	}

	public void addSong(String addSong, String toPlaylist) {
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "INSERT INTO SONGS (SONG, PLAYLIST) VALUES (\""+addSong+"\", \""+toPlaylist+"\")";
		try {
		con.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
	}

	public void addLocation(String loc) {
		if (loc.equals("")){
			   return;
		}
		ZDBConnection con = ZDBConnection.getInstance();
		String splitLoc[] = loc.split("X"); //FORMAT = ID#LAT#LONG (#==X)
		int id = Integer.parseInt(splitLoc[0]);
                String query = "UPDATE GPS_ZEEK1 SET LAT = "+splitLoc[1]+" , LON = "+splitLoc[2]+", DATE = now() WHERE id = "+id;
		try {
		con.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		con.close();
		
	}

	public String getLocationString(int id) {
		String response = ""; //FORAMT LAT#LON#DATE
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "SELECT LAT, LON, DATE FROM GPS_ZEEK1 WHERE ID ="+id;
		try {
		ResultSet rs = con.executeQuery(query);
		while (rs.next()){
			response+=rs.getDouble(1)+"#";
			response+=rs.getDouble(2)+"#";
			response+=rs.getString(3);
		}
		
		//_listMap.put(item, list);
		}catch(Exception e){
			response = "error";
			e.printStackTrace();
		}
		con.close();
		return response;
	}

	public String getModuleTarget(String req) {
		String response = "error.html"; //FORAMT LAT#LON#DATE
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "SELECT MODULE_JSP FROM MODULES WHERE MODULE_NAME = '"+req+"'";
		try {
		ResultSet rs = con.executeQuery(query);
		if (rs.next()){
			response = rs.getString(1);
		}
		
		//_listMap.put(item, list);
		}catch(Exception e){
			response = "error.html";
			e.printStackTrace();
		}
		con.close();
		return response;
	}
	
	
	/* adds fb profile and returns new user_id */
	public int bot_addNewUser(FBProfile profile, String sender) {
		int user_id = -1;
		ZDBConnection con = ZDBConnection.getInstance();
		String fname = profile.getFirstName();
		String lname = profile.getLastName();
		String gender = profile.getGender();
		String locale = profile.getLocale();
		String tz = profile.getTimezone().toString();
		String pic = profile.getProfilePic();
		
		String query = insertBotFbUser_template;
		
		query = query.replace("FB_SENDER", sender);
		query = query.replace("FB_FNAME", fname);
		query = query.replace("FB_LNAME", lname);
		query = query.replace("FB_GENDER", gender);
		query = query.replace("FB_LOCALE", locale);
		query = query.replace("FB_TZ", tz);
		query = query.replace("FB_PIC", pic);
		
		
		try {
			int r = con.executeUpdate(query);
		}catch(Exception e){
			e.printStackTrace();
			con.close();
			return user_id;
		}
		con.close();
		return bot_getUserId(sender);
	}

	public boolean bot_checkExistingUser(String sender) {
		String query = "SELECT * FROM BOT_FB_USER WHERE FB_USER_ID = '"+sender+"'";
		ZDBConnection con = ZDBConnection.getInstance();
		try{
			ResultSet rs = con.executeQuery(query);
			if (rs.next()){
				return true;
			} else {
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			con.close();
			return false;
		}finally {
			con.close();
		}
	}
	
	public int bot_getUserId(String sender){
		int user_id = -1;
		ZDBConnection con = ZDBConnection.getInstance();
		String query = "SELECT USER_ID FROM BOT_FB_USER WHERE FB_USER_ID='"+sender+"'";

		try {
			ResultSet rs = con.executeQuery(query);
			rs.next();
			user_id = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
			con.close();
			return user_id;
		}		
		return user_id;
	}
	
	
	
}
