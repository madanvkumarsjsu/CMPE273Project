import org.boon.core.Handler;
import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.EtcdClient;
import org.boon.etcd.Response;
import org.boon.json.JsonCreator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static org.boon.Boon.iterator;
import static org.boon.Boon.puts;

/**
 * Created by sindhu on 12/5/14.
 */

@Controller
@RequestMapping("/v1/api")
public class ServiceDiscovery {

    @RequestMapping(method=RequestMethod.GET,produces="application/json")
    public @ResponseBody String getServiceNameDirectory() throws JSONException {
        System.out.println("inside get services method...........");
        Etcd client = ClientBuilder.builder().hosts(URI.create("http://54.149.70.109:4001")).createClient();
        Response response = client.listRecursive("");
        String responseString = response.toString();
        System.out.println(">>>>>>>>>>>>>>>respones string>>>>>>>>>>>>>" + responseString);
        JSONObject responseJO = new JSONObject(responseString);
        JSONObject joNode = responseJO.getJSONObject("node");
        JSONArray jArrayNodes = joNode.getJSONArray("nodes");

        JSONObject rootApplicationsObject = jArrayNodes.getJSONObject(0); // This is the applications object
        System.out.println(">>>rootApplicationsObject:key" + rootApplicationsObject.get("key").toString());

        JSONObject jsonRootResult = new JSONObject();
        JSONArray jsonApplicationResultArray = new JSONArray();
        JSONArray applicationJsonArray = rootApplicationsObject.getJSONArray("nodes");

        JSONArray jsonServicesArray = new JSONArray();

        for (int j = 0; j < applicationJsonArray.length(); j++) {
            JSONObject serviceJo = applicationJsonArray.getJSONObject(j);
            String serviceDBName = serviceJo.get("key").toString();
            JSONArray servieDBInstanceJsonArray = serviceJo.getJSONArray("nodes");
            System.out.println("Key-----Array: " + serviceDBName + "  ," + servieDBInstanceJsonArray);
            for (int k = 0; k < servieDBInstanceJsonArray.length(); k++) {
                JSONObject serviceInstanceJo = servieDBInstanceJsonArray.getJSONObject(k);
                String serviceName = serviceInstanceJo.get("key").toString();
                JSONArray replicaArray = serviceInstanceJo.getJSONArray("nodes");
                System.out.println("Key-----Array: " + serviceName + "  ," + replicaArray);
                JSONArray jsonReplicaArrayResult = new JSONArray();
                for (int l = 0; l < replicaArray.length(); l++) {
                    JSONObject instanceJo = replicaArray.getJSONObject(l);
                    String instanceName = instanceJo.get("key").toString();
                    JSONObject jsonReplicaResult = new JSONObject();
                    Boolean bol = true;
                    if (instanceJo.has("key") && instanceJo.has("value")) {
                        System.out.println("Key-----Value: " + instanceName + "  ," + instanceJo.get("value").toString());
                        String str = instanceJo.get("value").toString();
                        try {
                            JSONObject val = new JSONObject(str);
                            Iterator<JSONObject> keys = val.keys();
                            while( keys.hasNext() ){
                                String key = keys.next().toString();
                                if( val.get(key) instanceof JSONObject ){

                                    jsonReplicaResult.put(key, val.get("value"));
                                }
                            }
                        }catch(Exception e){

                             bol = false;
                        }

                        if(!bol)
                        {
                            jsonReplicaResult.put("value", instanceJo.get("value").toString());
                        }

                    }
                    jsonReplicaResult.put("replicaName", instanceName);
                    jsonReplicaArrayResult.put(jsonReplicaResult);

                }

                JSONObject jsonServiceResult = new JSONObject();
                jsonServiceResult.put("serviceName", serviceName);
                jsonServiceResult.put("replicas", jsonReplicaArrayResult);
                jsonServicesArray.put(jsonServiceResult);

            } // service Array of application will be ready.

            JSONObject jsonApplicationResult = new JSONObject();
            jsonApplicationResult.put("applicationName", serviceDBName);
            jsonApplicationResult.put("services", jsonServicesArray);
            jsonApplicationResultArray.put(jsonApplicationResult);


        }

        jsonRootResult.put("applications", jsonApplicationResultArray);
        jsonRootResult.put("rootName", rootApplicationsObject.get("key").toString());

        System.out.println("end of jsonRootResult");


        return jsonRootResult.toString();
    }




}
