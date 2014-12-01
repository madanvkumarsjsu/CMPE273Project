package etcdClient;


import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.boon.etcd.Node;

import java.net.URI;

import static org.boon.Boon.puts;
import static org.boon.Exceptions.die;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.json.JSONObject;
import java.util.*;
import java.io.*;


@Controller
@RequestMapping("/v1/api")
public class loadBalancer {

    Etcd client;
    loadBalancer(){
        Response response;
        JSONObject jo = new JSONObject();
        try{jo.put("ip addr", "127.0.0.1");
        jo.put("port", "8080");
        }catch(Exception e){

        }
        String value = jo.toString();
        System.out.println(value);
        etcdSmartClient client = new etcdSmartClient("loadbalancer",value,"loadbalancer");
        client.Start();
        //client.getAllKeys();

    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String getReq(){
      
      Response response = client.listRecursive("Services");
      Node firstNode = response.node();
      List<Node> nodes = response.node().getNodes();
      nodes.add(firstNode); 
      List<String> keyJson = new ArrayList<String>();
      for(ListIterator<Node> iter = nodes.listIterator(); iter.hasNext(); ) {
        Node node = iter.next();
        keyJson.add(node.getValue());
        System.out.println(node.getValue());
      }
      return("done");
    }

}

