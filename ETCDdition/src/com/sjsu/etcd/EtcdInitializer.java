package com.sjsu.etcd;

import java.net.URI;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;

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




}
