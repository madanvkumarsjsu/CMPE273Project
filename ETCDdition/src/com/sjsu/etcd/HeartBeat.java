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

public class HeartBeat extends Thread {

	String strService = null;
	public HeartBeat(String strService){
		this.strService = strService;
	}
	Etcd client = ClientBuilder.builder().hosts(URI.create("http://localhost:4001")).createClient();

//	public static void main(String args[]){
//		Response response = client.setTemp("mysampleapplication1","applicationhost",20);
//		puts(response);
//	}
	
	
	public void run(){
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			Response response = null;
			@Override
			public void run() {
				response = client.setTemp("mysampleapplication1","applicationhost",20);
				strService = "database";
				boolean isAlive = ping(strService);
				if(isAlive){
					response = client.setTemp("mysampleapplication1/"+strService,"ds051740.mongolab.com",20);
					puts(response);				
				}
			}
		}, 0, 15000);
	}
	
	private boolean ping(String strService){
		boolean isAlive = false;
		if("database".equalsIgnoreCase(strService)){
			String strServiceHost = "ds051740.mongolab.com";
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
					isAlive = true;
				}			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return isAlive;
		
	}
}
