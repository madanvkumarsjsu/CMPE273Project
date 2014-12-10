package com.etcd.pkg;

import static org.boon.Boon.puts;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Node;
import org.boon.etcd.Response;

import java.net.URI;
import java.util.*;

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
		
		 Etcd client = ClientBuilder.builder().hosts(
//	             URI.create("http://54.149.70.109:4001")
	              URI.create("http://127.0.0.1:4001")
	     ).createClient();
		 
		 Response resp = client.listRecursive("application");
	    	
	    	
	    	Map<String, String> map = new HashMap<String,String>();
	    	
	    	ArrayList<String> listTree = new ArrayList<String>();
	    		
	    	ArrayList<String> listRun = new ArrayList<String>();
	    		    	
	    	ArrayList<String> listStop = new ArrayList<String>();
	    	
	    	ArrayList<String> listStopFormat = new ArrayList<String>();
	    		
	    	ArrayList<String> TreeRun = new ArrayList<String>();
	    	
	    	ArrayList<String> TreeStop = new ArrayList<String>();

	    	List<Node> nodesList = resp.node().getNodes();
	    	
	    	//Parsing etcd 
	    	
/*	    	for (int i=0;i<nodesList.size();i++) {
	    		Node a = nodesList.get(i);
	    		try {
	    		List<Node> nodesListA = a.getNodes();
	    		listTree.add(a.key());
	    		listRun.add(a.key());
	    		for(int j=0;j<nodesListA.size();j++) {
	    			Node s = nodesListA.get(j);
	    			try {
	    			List<Node> nodesListS = s.getNodes();
	    			listTree.add(s.key());
	    			listRun.add(s.key());
	    			for(int k=0;k<nodesListS.size();k++) {
	    				Node r = nodesListS.get(k);
	    				try {
	    				List<Node> nodesListR = r.getNodes();
	    				listTree.add(r.key());
	    				listRun.add(r.key());
	    				for(int l =0;l<nodesListR.size();l++) {
	    					try {
	        					map.put(nodesListR.get(l).key(), nodesListR.get(l).getValue());
	        					String keyVal = (nodesListR.get(l).key()+" = "+ nodesListR.get(l).getValue());
	        					listTree.add(keyVal);
	    					}
	    					catch(Exception e)
	    					{
	    						System.out.println("No properties! Can not get values");
	    					}
	        				}
	    				}
	    				catch(Exception e)
	    				{
	    					System.out.println("No properties for"+r.key());
	    				}
	    				}
	    			}
	    			catch(Exception e)
					{
	    				System.out.println("No replicas for"+s.key());
					}
	    			}
	    		}
	    		catch(Exception e)
				{
	    			System.out.println("No services for"+a.key());
				}
	    		}
	    */
	    	for (int i=0;i<nodesList.size();i++) {
	    		Node a = nodesList.get(i);
	    		try {
	    		List<Node> nodesListA = a.getNodes();
	    		listTree.add(a.key());
	    		listRun.add(a.key());
	    		for(int j=0;j<nodesListA.size();j++) {
	    			Node s = nodesListA.get(j);
	    			try {
	    			List<Node> nodesListS = s.getNodes();
	    			listTree.add(s.key());
	    			listRun.add(s.key());
	    			for(int l =0;l<nodesListS.size();l++) {
    					try {    						
    						String lkey = nodesListS.get(l).key();
    						int index1 = lkey.lastIndexOf('/');
    						lkey = lkey.substring(index1+1);
    						if(lkey.equals("leader")){
    							String keyVal = (nodesListS.get(l).key()+" = "+ nodesListS.get(l).getValue());
    							listTree.add(keyVal); }
    						else {
    							String jsonString = nodesListS.get(l).getValue();
    							JSONObject jobj = new JSONObject(jsonString);
    							listTree.add(nodesListS.get(l).key());
    							listTree.add((nodesListS.get(l).key())+"/AppName"+" = "+((String)jobj.get("AppName")));
    							listTree.add((nodesListS.get(l).key())+"/port"+" = "+Integer.toString((Integer)jobj.get("port")));
    							listTree.add((nodesListS.get(l).key())+"/HostName"+" = "+((String)jobj.get("HostName")));
    						}
    						listRun.add(nodesListS.get(l).key());	
    					}
    					catch(Exception e)
    					{
    						System.out.println("No properties! Can not get values");
    					}
	    				
	        		}	    				
	    			}
	    			catch(Exception e)
					{
	    				System.out.println("No replicas for"+s.key());
					}
	    		}
	    		}
	    		catch(Exception e)
				{
	    			System.out.println("No services for"+a.key());
				}
	    	}
	    	
	//    	client.set("All", listRun.toString());
/*	    for( Iterator entries = map.entrySet().iterator(); entries.hasNext();){

		    Entry entry = (Entry) entries.next();
		    	
		    runList.add((String) entry.getKey());
		 } */
		    
/*		 Response tryset = client.get("mapAll");	
		 String arrayVal = tryset.node().getValue();		 
		 String[] alist = arrayVal.split(",");

		 String s1 = alist[0].substring(1);
		 alist[0] = s1;
		 int ind = alist[alist.length-1].indexOf(']');
		 s1 = alist[alist.length-1].substring(1, ind);
		 alist[alist.length-1] = s1;
		 for(int i =1;i<alist.length-1;i++) {
		 s1 = alist[i].substring(1);
		 alist[i] = s1;
		 }
	
		 ArrayList<String> AllList = new ArrayList<String>(Arrays.asList(alist));
		 
		 for(int i=0;i<AllList.size();i++) {
			 if(runList.contains(AllList.get(i))){
				 
			 }
			 else {
				 stopList.add(AllList.get(i));
			 }
		 }
		 
		 for(int i =0;i<runList.size();i++) {
			 if(AllList.contains(runList.get(i))) {
				 
			 }
			 else {
				AllList.add(runList.get(i)); 
			 }
		 } */
		
	    	Response getAll = client.get("All");	
			 String AllVal = getAll.node().getValue();		 
			 String[] alist = AllVal.split(",");

			 String s1 = alist[0].substring(1);
			 alist[0] = s1;
			 int ind = alist[alist.length-1].indexOf(']');
			 s1 = alist[alist.length-1].substring(1, ind);
			 alist[alist.length-1] = s1;
			 for(int i =1;i<alist.length-1;i++) {
			 s1 = alist[i].substring(1);
			 alist[i] = s1;
			 }
		
			 ArrayList<String> listAll = new ArrayList<String>(Arrays.asList(alist));
			 
			 for(int i=0;i<listAll.size();i++) {
				 if(listRun.contains(listAll.get(i))){
					 
				 }
				 else {
					 listStop.add(listAll.get(i));
				 }
			 }
			 
			 for(int i =0;i<listRun.size();i++) {
				 if(listAll.contains(listRun.get(i))) {
					 
				 }
				 else {
					listAll.add(listRun.get(i)); 
				 }
			 } 	
			 client.set("All",listAll.toString());
		 
			//To represent running services in a tree structure
		    	
		    	Collections.sort(listTree, new Comparator<String>(){
		    		@Override public int compare(String o1, String o2) {
		    		return o1.compareTo(o2);
		    		}
		    		});

		    	for(String s:listTree){
		    		int length = s.split("/").length - 1;
		    		StringBuilder sb = new StringBuilder();
		    		sb.append("|");
		    		for(int i=0;i<length-1;i++)
		    			sb.append("&nbsp;"+"_");
		    		String look = sb.toString();
		    		int c = look.lastIndexOf('_');
		    		
		    		look=look.substring(0,c-1) +"|__"+look.substring(c);
					int index1 = s.lastIndexOf('/');
					String str = s.substring(index1+1);
					
					s = look.concat(str);
					TreeRun.add(s);
		    	}
		   
		   //Formatting to form stop tree
		    	
		    	for (String s:listStop) {
			    	ArrayList<Integer> intArray = new ArrayList<Integer>();
					int i = s.indexOf('/');
					while(i>=0){
						intArray.add(i);
						i = s.indexOf('/',i+1);
					}
						String tempStr = "";
						for(int k=2;k<intArray.size();k++) {
							tempStr = s.substring(intArray.get(0), intArray.get(k));
						}
						if(!(listStopFormat.contains(tempStr)))
						listStopFormat.add(tempStr); 
						if(!(listStopFormat.contains(s)))
						listStopFormat.add(s);
				
		    	}
		    	
		//To represent Stopped services in a tree structure
		
			 Collections.sort(listStopFormat, new Comparator<String>(){
		    		@Override public int compare(String o1, String o2) {
		    		return o1.compareTo(o2);
		    		}
		    		});
			 
		    	for(String s:listStopFormat){
		    		int length = s.split("/").length - 1;
		    		StringBuilder sb = new StringBuilder();
		    		sb.append("|");
		    		for(int i=0;i<length-1;i++)
		    			sb.append("&nbsp;"+"_");
		    		String look = sb.toString();
		    		int c = look.lastIndexOf('_');
		    		look=look.substring(0,c-1) +"|__"+look.substring(c);
					int index2 = s.lastIndexOf('/');
					String str = s.substring(index2+1);
					
					s = look.concat(str);
					TreeStop.add(s);
		    	} 
		
//	    request.setAttribute("map", map);
	    request.setAttribute("TreeStop", TreeStop);
	    request.setAttribute("TreeRun", TreeRun);
	     
	    RequestDispatcher rd = request.getRequestDispatcher("services.jsp");
	    rd.forward(request, response); 
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	
}
