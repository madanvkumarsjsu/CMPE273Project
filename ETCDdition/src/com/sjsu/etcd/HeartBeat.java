package com.sjsu.etcd;

import static org.boon.Boon.puts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.boon.core.Handler;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class HeartBeat extends Thread {

	String strServiceName 	= null;
	String strHostName 		= null;
	String strHostPort 		= null;
	String strKey 			= null;
	String strValue 		= null;
	Etcd client 			= null;


	public HeartBeat(Etcd etcdClient, String strServiceName, String strServiceHost, String strServicePort){
		this.client 			= etcdClient;
		this.strServiceName 	= strServiceName;
		this.strKey				= "applications/"+strServiceName;
		this.strHostName		= strServiceHost;
		this.strHostPort		= strServicePort;
		this.strValue 			= generateValue();
		System.out.println("key::::"+ strKey);
		System.out.println("value::::"+ strValue);
	}

	private String generateValue(){
		String strValue = null;
		JSONObject jo = new JSONObject();
		try {
			jo.put("App Name", strServiceName);
			jo.put("Host Name", strHostName);
			jo.put("port", strHostPort);
			strValue = jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strValue;

	}

	//	public static void main(String args[]){
	//		client = ClientBuilder.builder().hosts(URI.create("http://localhost:4001")).createClient();
	//		Response response = client.setTemp("mysampleapplication1","applicationhost",20);
	//		puts(response);
	//	}

	public void run(){

		try{
		client.setTemp(strKey, strValue, 15);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				boolean isAlive = ping(strHostName);
				if(isAlive){
					try{
						client.setTemp(strKey,strValue,20);
					}
					catch(Exception ex){
						System.out.println("Exception in inserting configuration to ETCD"+ ex.getMessage());
						ex.printStackTrace();
					}
				}
				else{
					String strDir = strKey.substring(0, strKey.lastIndexOf("/"));
					String strReplica = strKey.substring(strKey.lastIndexOf("/")+1, strKey.length());
					String strLeader = strDir+"/leader";
					String strLeaderVal = client.get(strLeader).node().getValue();
					if(strLeaderVal.equalsIgnoreCase(strReplica)){
						ServiceXMLParser.hmService.get(strDir).electLeader();
					}
				}
			}
		}, 0, 15000);
		}
		catch(Exception ex){
			System.out.println("Exception in Heartbeat:::::"+ex.getMessage());
			ex.printStackTrace();
		}
	}

	private boolean ping(String strServiceHost){
		boolean isAlive = false;
		if(strServiceHost != null && !"".equalsIgnoreCase(strServiceHost)){
			try {
				Process ping = Runtime.getRuntime().exec("ping " + strServiceHost+" -c 4");
				BufferedReader br = new BufferedReader(new InputStreamReader(ping.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				if(sb.toString().indexOf("0% packet loss") != -1 && sb.toString().indexOf("0 received") == -1){
					System.out.println(sb.toString());
					isAlive = true;
				}			
			} catch (Exception e) {
				System.out.println("HeartBeat:::Exception in heartbeat execution. Check host name::::"+e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println(strServiceHost+">>>>isAlive>>>>"+isAlive);
		return isAlive;

	}
}
