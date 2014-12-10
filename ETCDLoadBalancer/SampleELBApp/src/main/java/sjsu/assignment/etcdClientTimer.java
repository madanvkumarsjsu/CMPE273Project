package sjsu.assignment;

import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;

import static org.boon.Boon.puts;
import static org.boon.Exceptions.die;

import java.io.*;
import java.net.URI;
import java.util.*;


class etcdClientTimer{
	
	Etcd client;
	String key;
	String value;
	String path;
	
	etcdClientTimer(String key,String value,String path,Etcd client){
		this.key = key;
		this.value=value;
		this.path = path;
    this.client = client;
	}

	class Task extends TimerTask {

	    int ttl=20;
	    Response response;
	    // run is a abstract method that defines task performed at scheduled time.
	    public void run() {
	    	response = client.setTemp(path+"/"+key,value,ttl);
	      //  puts(response);
	    }
	}
	
	public void Start(){
		 Timer timer = new Timer();	
		 int keyRefreshTime = 15000;
		 Response response;
	   // Schedule to run after every 3 second(3000 millisecond)
	   timer.schedule( new Task(),0,3000); 	 
	}	
}
