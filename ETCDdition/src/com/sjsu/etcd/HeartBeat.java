package com.sjsu.etcd;

import static org.boon.Boon.puts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.boon.etcd.ClientBuilder;
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

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			Response response = null;
			@Override
			public void run() {
				boolean isAlive = ping(strHostName);
				if(isAlive){
					response = client.setTemp(strKey,strValue,20);
					puts(response);				
				}
			}
		}, 0, 15000);
	}

	private boolean ping(String strServiceHost){
		boolean isAlive = false;
		if(strServiceHost != null && !"".equalsIgnoreCase(strServiceHost)){
			try {
				Process ping = Runtime.getRuntime().exec("ping " + strServiceHost);
				BufferedReader br = new BufferedReader(new InputStreamReader(ping.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				if(sb.toString().indexOf("Reply") != -1){
					System.out.println(sb.toString());
					isAlive = true;
				}			
			} catch (IOException e) {
				System.out.println("HeartBeat:::Exception in heartbeat execution. Check host name");
				e.printStackTrace();
			}
		}

		return isAlive;

	}
}
