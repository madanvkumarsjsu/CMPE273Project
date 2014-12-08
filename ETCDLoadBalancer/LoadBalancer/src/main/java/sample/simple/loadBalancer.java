package sample.simple;

import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.boon.etcd.Node;

import java.net.*;
import static org.boon.Boon.puts;
import static org.boon.Exceptions.die;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.json.JSONObject;
import java.util.*;
import java.io.*;


@Controller
@RequestMapping("/v1/api")
public class loadBalancer{

	Etcd client; 
	ConcurrentLinkedQueue<String> serverQueue;

	loadBalancer() {
		System.out.println("inside loadbalancer");
		Response response;
		client = ClientBuilder.builder().hosts(
				URI.create("http://localhost:4001")).createClient();
		serverQueue = new ConcurrentLinkedQueue<String>();
	}


	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> onRequest(){
		String nextServer = null;
		String strKey = null;
		ResponseEntity<String> result=null;
		Response response = client.listRecursive("Services");
		puts(response);
		List<Node> activeNodes = response.node().getNodes();
		System.out.println("activeNodes>>>>>"+activeNodes);
		try{
			if(activeNodes.isEmpty()){
				System.out.println("No services available");
				return(result);
			}
			for(int i = 0;i<activeNodes.size();i++) {
				Node node = activeNodes.get(i);
				System.out.println("Node>>>>>"+node+" "+node.getValue());
				if(!serverQueue.contains(node.key()))
				{
					System.out.println("adding node which is not present in serverQ");
					serverQueue.add(node.key());   
					System.out.println("serverQueue############"+serverQueue+" "+serverQueue.size());
				}
				System.out.println(node.getValue());
			} 
			while(!(serverQueue.isEmpty()))
			{
				nextServer = serverQueue.poll();
				System.out.println(nextServer);
				strKey = nextServer.substring(1);
				if((client.get(strKey)).node()==null){
					System.out.println("Removing service from queue: it is not active");
				}
				else{
					System.out.println("Got the active server:");
					break;
				}
			}


			String serverAddr = (client.get(strKey)).node().getValue();
			System.out.println(serverAddr);
			JSONObject server = new JSONObject(serverAddr);
			System.out.println(server);

			String url_string = "http://"+server.getString("hostname")+":"+server.getString("port") + "/count";
			System.out.println(url_string);

			RestTemplate restTemplate = new RestTemplate();
			result = restTemplate.getForEntity(url_string,String.class);
			System.out.println(result);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;

	}
}

