package sample.simple;

import static org.boon.Boon.puts;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sjsu.etcd.EtcdInitializer;
import com.sjsu.etcd.HeartBeat;
import com.sjsu.etcd.ServiceXMLParser;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SampleSimpleApplication {

	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SampleSimpleApplication.class, args);
		//File f = new File("src/main/resource/application.properties");
		Resource res = ctx.getResource("classpath:com/sjsu/etcdres/appservices.xml");
		try {
			System.out.println(res.getFile());
			EtcdInitializer client = new EtcdInitializer("localhost");
			ServiceXMLParser sxp = new ServiceXMLParser(client.getClient(),res.getFile());// for testing
			sxp.parseServiceXML();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}