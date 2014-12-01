package sample.simple;

import static org.boon.Boon.puts;

import java.net.URI;
import java.util.*;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
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
                Response response1 = client.createTempDir(hostName, 10);
				puts(response1);
                Response response2 = client.set(hostName+"/HostAddress", hostAddr);
                puts(response2);
                Response response3 = client.set(hostName+"/PortNumber", portNumber);
                puts(response3);

            }
		}, 0, 15000);
	}

}