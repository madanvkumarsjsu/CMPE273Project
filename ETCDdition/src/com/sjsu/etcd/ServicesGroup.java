package com.sjsu.etcd;

import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import java.lang.String;
import java.util.ArrayList;


public class ServicesGroup{

    Etcd client;
    String serviceDirectory;
    ArrayList<String> serviceList;
    String currentLeader;

    public ServicesGroup(Etcd client, String dir, ArrayList<String> list){

        this.serviceDirectory = dir;
        this.serviceList = list;
        this.client = client;

    }

    /*
    * check key for each service from etcd.
    * call this from a separate thread or timer in main class
    * */
    public String electLeader(){

        Response response;
        String key = null;
        
        for (String service: serviceList){
        	
        	key = serviceDirectory+"/"+service;
            response = client.get(key);

            if(response.node()== null){
               continue;
            }
            else{
                //make this service current leader
               currentLeader = service;
               key = serviceDirectory+"/leader";
               System.out.println(key);
               System.out.println(currentLeader);
               
               client.set(key, currentLeader);
               break;
            }

        }
        return currentLeader;

    }

    /*
     * check if current leader is up
     */
    public boolean checkLeaderStatus(){
        Response response;
        response = client.get(currentLeader);
        if(response.node()== null){
            return false;
        }
        else{
            return true;
        }
    }

    public String getServiceDirectory() {
        return serviceDirectory;
    }

    public void setServiceDirectory(String serviceDirectory) {
        this.serviceDirectory = serviceDirectory;
    }

    public ArrayList<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<String> serviceList) {
        this.serviceList = serviceList;
    }

    public String getCurrentLeader() {
        return currentLeader;
    }

    public void setCurrentLeader(String currentLeader) {
        this.currentLeader = currentLeader;
    }
}