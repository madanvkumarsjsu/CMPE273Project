import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 * Created by sindhu on 12/7/14.
 */


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MyClient {

    public static void main(String args[]) throws JSONException, MalformedURLException {
//        ServiceDiscovery sd = new ServiceDiscovery();
//        String nameDirectory = null;
       // try {
//            nameDirectory = sd.getServiceNameDirectory();
        ApplicationContext ctx = SpringApplication.run(MyClient.class, args);

//        System.out.println("Inside myclient printing name directory services........... "+nameDirectory.toString());
        //SpringApplication.run(MyClient.class, args);
        //boolean ok;


    }

}
