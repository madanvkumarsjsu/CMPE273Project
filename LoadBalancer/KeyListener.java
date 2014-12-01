

import org.boon.core.Handler;
import org.boon.core.Sys;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.boon.etcd.EtcdClient;
import org.boon.etcd.Response;
import java.net.URI;

/**
 * Created by Rajalakshmi on 11/29/2014.
 */
public class KeyListener implements  Runnable{

    private  String key = null;
    private Etcd client = null;
    public KeyListener(String key, Etcd client){
        this.key = key;
        this.client = client;

    }

    @Override
    public void run() {
        //asynchronous calls to get key after TTL + constant time
        System.out.println("key is "+key);
        client.get(handler,key);
        Sys.sleep(3000);
    }

    Handler<Response> handler = new Handler<Response>() {
        @Override
        public void handle(Response event) {

            System.out.println("in handler. event "+event);
            if (event.node() != null) {
                //  service is up
                System.out.println("Service up");
                client.get(handler, key);
                Sys.sleep(3000);

            } else {
                // service is down. Update UI dashboard
                System.out.println("Service down");
                return;
            }


        }
    };

}
