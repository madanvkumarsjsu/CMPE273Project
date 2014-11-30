package com.etcd.pkg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
//import org.boon.etcd.Node;

import java.net.URI;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
//import java.util.Map.Entry;

import org.json.*;

/**
 * Servlet implementation class Srvlt
 */
@WebServlet("/Srvlt")
public class Srvlt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Srvlt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		
		 Etcd client = ClientBuilder.builder().hosts(
	             URI.create("http://localhost:4001")
	     ).createClient();
		 
		 Response res = client.list("");
	        
//	     out.println(res);
	     
	     String result = res.toString();
	     
	     JSONObject jsonObject = new JSONObject(result);
	     
	     int resCode = jsonObject.getInt("responseCode");
	     
	     Boolean created = jsonObject.getBoolean("created");
	     
	     out.println(resCode);
	 
	     String node = jsonObject.get("node").toString();
	 
	     JSONObject jsonObject2 = new JSONObject(node);
	 
	     JSONArray jArray = jsonObject2.getJSONArray("nodes");
	     
	     Map<String, String> map = new HashMap<String,String>();
	     
	     for(int i=0; i< jArray.length();i++) 
	     {
	    	 JSONObject jsonObject3 = jArray.getJSONObject(i);
	    	 String val = jsonObject3.getString("value");
	    	 String key = jsonObject3.getString("key");
	    	 
	    	 map.put(key, val);
		 
		 out.println("key:"+key+","+"val:"+val);
		 		
	     }
	     
/*	     for( Iterator entries = map.entrySet().iterator(); entries.hasNext();){

	    	  Entry entry = (Entry) entries.next();

	    	  System.out.println(entry.getKey() + "," + entry.getValue());
	     } */
	     
	     request.setAttribute("rows", jArray.length());
	     request.setAttribute("map", map);
	     RequestDispatcher rd = request.getRequestDispatcher("index2.jsp");
	     rd.forward(request, response); 
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
