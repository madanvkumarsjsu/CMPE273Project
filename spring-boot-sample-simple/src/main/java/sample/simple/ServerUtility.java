package sample.simple;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.servlet.*;

public class ServerUtility {
	
	InetAddress IP = null;
    Properties p = null;


	public ServerUtility(){

        try {
			IP=InetAddress.getLocalHost();
            p = System.getProperties();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public String getHostName(){

        return IP.getHostName();
	}

	public String getHostAddress(){

        return IP.getHostAddress();
	}

    public String getPortNumber(){
        String portNumber = null;
        if(p.get("server.port").equals(null))
            portNumber = "8080";
        else
            portNumber = (String) p.get("server.port");

        return portNumber;
    }
	

}
