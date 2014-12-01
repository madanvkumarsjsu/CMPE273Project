package etcdClient;

import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;

import static org.boon.Boon.puts;
import static org.boon.Exceptions.die;

import java.io.*;
import java.net.URI;
import java.util.*;


class etcdSmartClient{
	
	Etcd client;
	String key;
	String value;
	String path;
	
	etcdSmartClient(String key,String value,String path){
		client = ClientBuilder.builder().hosts(
                URI.create("http://localhost:4001")).createClient();
		this.key = key;
		this.value=value;
		this.path = path;
	}

	class Task extends TimerTask {

	    int ttl=20;
	    Response response;
	    // run is a abstract method that defines task performed at scheduled time.
	    public void run() {
	    	response = client.setTemp(path+"/"+key,value,ttl);
	        puts(response);
	    }
	}
	
	public void Start(){
		 Timer timer = new Timer();
		 int ttl=20;
		 int keyRefreshTime = 15000;
		 Response response;
		 if(path!=null)
		 {
			 response = client.createDir(path);
			 puts(response);
		 }

	       // Schedule to run after every 3 second(3000 millisecond)
	       timer.schedule( new Task(),0,3000); 
		 
	     //timer.schedule(new TimerTask() {
				
				//@Override
				//public void run() {
					//response = client.setTemp(path+"/"+key,value,ttl);
			        //puts(response);				
				//}
			//}, 0, keyRefreshTime);
	}
	
	public void getAllKeys(){
		Response response = client.listRecursive(path);
		puts(response);
	}
}