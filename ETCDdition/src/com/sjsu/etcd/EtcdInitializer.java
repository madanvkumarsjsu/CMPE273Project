package com.sjsu.etcd;

import java.net.URI;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.json.JSONObject;

import com.sjsu.etcd.bean.ServiceObject;

public class EtcdInitializer {

	private Etcd client = null;
	public Etcd getClient() {
		return client;
	}
	public EtcdInitializer(String hostName) {

		String etcdurl = "http://"+hostName+":4001";
		try{
			client = ClientBuilder.builder().hosts(URI.create(etcdurl)).createClient();
		}
		catch(Exception ex){
			System.out.println("EtcdInitializer:::Exception while fetching ETCD connection. Check host name and try again.");
		}
	}

	public ServiceObject getService(Etcd client, String strApp, String strServiceIdentifier){
		ServiceObject objServiceObject = null;
		try{
			objServiceObject = new ServiceObject();
			String strValue = client.get("applications"+"/"+strApp+"/"+strServiceIdentifier+"/leader").node().getValue();
			String serviceAttr = client.get("applications"+"/"+strApp+"/"+strServiceIdentifier+"/"+strValue).node().getValue();
			JSONObject json = new JSONObject(serviceAttr);
			objServiceObject.setStrAppName((String)json.get("App Name"));
			objServiceObject.setStrHost((String)json.get("port"));
			objServiceObject.setStrPort((String)json.get("Host Name"));
		}
		catch(Exception ex){
			System.out.println("Exception::: Cannot find service instance in ETCD");
			ex.printStackTrace();
		}
		return objServiceObject;
	}
}
