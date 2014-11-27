package sample.simple;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerUtility {
	
	InetAddress IP = null;
	public ServerUtility(){
		try {
			IP=InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public String getHostName(){
		return IP.getHostName();
	}
	public String getHostAddress(){
		return IP.getHostName();
	}
	

}
