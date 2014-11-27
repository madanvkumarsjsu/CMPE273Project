package sample.simple;

import static org.boon.Boon.puts;

import java.net.URI;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
		ApplicationContext ctx = SpringApplication.run(SampleSimpleApplication.class, args);
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		final Etcd client = ClientBuilder.builder().hosts(URI.create("http://localhost:4001")).createClient();

		ServerUtility su = new ServerUtility();
		String hostAddr = su.getHostAddress();
		String hostName = su.getHostName();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Response response = client.setTemp("application","mysampleapplication1" ,20);
				puts(response);				
			}
		}, 0, 15000);
	}

}