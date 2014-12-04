package sample.simple;

import static org.boon.Boon.puts;

import java.net.URI;
import java.util.*;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SampleSimpleApplication {

	public static void main(String[] args) {
System.out.println(">>>>>>>>>>Inside the main method");
        ServerUtility su = new ServerUtility();
        final String hostAddr = su.getHostAddress();
        final String hostName = su.getHostName();
        final String portNumber = su.getPortNumber();

        ApplicationContext ctx = SpringApplication.run(SampleSimpleApplication.class, args);
        final Etcd client = ClientBuilder.builder().hosts(URI.create("http://localhost:4001")).createClient();

        System.out.println(">>>>>>>>>>>>>starting the ttl");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
                System.out.println(new Date());
                JSONObject jo = new JSONObject();
                String value = null;
                try {
                    jo.put("ip address", hostAddr);
                    jo.put("port", portNumber);
                    jo.put("host name", hostName);
                    value = jo.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Response response2 = client.setTemp("spring-boot-simple-sample1/" + portNumber, value, 10);
                puts(response2);
            }
		}, 0, 15000);
	}

}