package sample.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.sjsu.etcd.EtcdInitializer;
import com.sjsu.etcd.ServiceXMLParser;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SampleSimpleApplication {

	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SampleSimpleApplication.class, args);
		//ETCD configuration start
		Resource res = ctx.getResource("classpath:com/sjsu/etcdres/appservices.xml");
		try {
			System.out.println(res.getFile());
			EtcdInitializer client = new EtcdInitializer("localhost");
			ServiceXMLParser sxp = new ServiceXMLParser(client.getClient(),res.getFile());
			sxp.parseServiceXML();
			client.getService(client.getClient(), sxp.getStrApplicationName(), "database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ETCD configuration End
	}

}