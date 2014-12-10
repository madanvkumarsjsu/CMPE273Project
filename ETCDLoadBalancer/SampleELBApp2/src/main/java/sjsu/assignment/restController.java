package sjsu.assignment;


import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;

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
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import javax.annotation.PostConstruct;

@Component
@Controller
@RequestMapping("/")
public class restController {

    Etcd client;
    etcdClientTimer timer;    
    @Value("${hostName}")
    private String hostName;
    @Value("${port}")
    private String port;
    @Value("${serviceName}")
    String serviceName;
    @Value("${servicePath}")
    private String servicePath;
    @Value("${etcdHost}")
    private String etcdHost;

    @PostConstruct
    public void initializeApp(){
        Response response;

        JSONObject jo = new JSONObject();
        try{jo.put("hostname", hostName);
        jo.put("port", port);
        }catch(Exception e){

        }
      
        client = ClientBuilder.builder().hosts(
                URI.create(etcdHost)
        ).createClient();
        System.out.println("hostname:" + hostName + " port:" + port + " Servicename:" + serviceName + " Service path:" + servicePath);
        response = client.createDir(servicePath);
        puts(response);
        timer = new etcdClientTimer(serviceName,jo.toString(),servicePath,client);
        timer.Start();        
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String getCount(){
          System.out.println("Success from:"+ serviceName + "\n");
        return("Success from:"+serviceName);
    }

}
