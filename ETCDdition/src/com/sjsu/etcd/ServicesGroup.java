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
        response = client.get(currentLeader);
        if(response.node()== null){
            //change leader
            for (String service: serviceList){
                response = client.get(service);

                if(response.node()== null){
                   continue;
                }
                else{
                    //make this service current leader
                   currentLeader = service;
                   break;
                }

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