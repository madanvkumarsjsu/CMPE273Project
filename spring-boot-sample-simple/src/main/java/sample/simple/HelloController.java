package sample.simple;

import java.net.InetAddress;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.Response;

import static org.boon.Boon.puts;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {

        return "Hello ETCD!";
    }

}