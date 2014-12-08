package loadBalancer;


import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.boon.etcd.Node;

import java.net.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

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
        Response response;
        client = ClientBuilder.builder().hosts(
                URI.create("http://localhost:4001")).createClient();
        serverQueue = new ConcurrentLinkedQueue<String>();
    }


    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> onRequest(){
      String nextServer = null;
      ResponseEntity<String> result=null;
      Response response = client.listRecursive("Services");
      List<Node> activeNodes = response.node().getNodes();
      //List<String> keyJson = new ArrayList<String>();
      if(activeNodes.isEmpty()){
        System.out.println("No services available");
        return(result);
      }
      for(ListIterator<Node> iter = activeNodes.listIterator(); iter.hasNext(); ) {
        Node node = iter.next();
        if(!serverQueue.contains(node.key()))
        {
          System.out.println("adding node which is not present in serverQ");
          serverQueue.add(node.key());   
        }
        //keyJson.add(node.getValue());
        System.out.println(node.getValue());
      } 
      while(!(serverQueue.isEmpty()))
      {
        nextServer = serverQueue.poll();
        System.out.println(nextServer);
        if((client.get(nextServer)).node()==null){
          //serverQueue.remove(nextServer);
          System.out.println("Removing service from queue: it is not active");
        }
        else{
          System.out.println("Got the active server:");
          break;
        }
      }
      
       
      String serverAddr = (client.get(nextServer.substring(1))).node().getValue();
      JSONObject server = new JSONObject(serverAddr);
      System.out.println(server);

      String url_string = "http://"+server.getString("hostname")+":"+server.getString("port") + "/count";
      System.out.println(url_string);

   RestTemplate restTemplate = new RestTemplate();
   result = restTemplate.getForEntity(url_string,String.class);

   return(result);

  }
}

