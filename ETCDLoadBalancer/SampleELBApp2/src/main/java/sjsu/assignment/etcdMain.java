package sjsu.assignment;


import java.net.URI;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rhightower on 10/8/14.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class etcdMain {


    public static void main(String... args) {

        SpringApplication.run(etcdMain.class, args);
        boolean ok;



    }

}

