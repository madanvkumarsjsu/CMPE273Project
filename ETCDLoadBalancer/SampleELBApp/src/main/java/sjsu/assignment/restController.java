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

@Controller
@RequestMapping("/count")
public class restController {

    Etcd client;        
    restController(){
        Response response;

        JSONObject jo = new JSONObject();
        try{jo.put("hostname", "127.0.0.1");
        jo.put("port", "8081");
        }catch(Exception e){

        }

        client = ClientBuilder.builder().hosts(
                URI.create("http://localhost:4001")
        ).createClient();

        response = client.createDir("Services");
        response = client.setIfNotExists("Services/service1", jo.toString());

        //puts(response);


        //response = client.get("count");

        //puts(response);
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String getCount(){
          System.out.println("Success");
        return("Success");
       /* int count;
        Response response = client.get("count");
        count = Integer.parseInt(response.node().getValue());
        count++;
        System.out.println("Count:" + count);
        client.set("count",Integer.toString(count));
        Response result = client.get("count");
        puts(result);
        return(result.node().getValue());
      */
    }

}
