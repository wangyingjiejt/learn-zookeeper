package zk.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.server.HelloRMIService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * 调用HelloRMIService的client
 * @user wangyj
 * @date 2018/3/8 11:06
 */
public class HelloClient {
    public static Logger logger= LoggerFactory.getLogger(HelloClient.class);

    public static void main(String[] args) {
        ServiceConsumer consumer= new ServiceConsumer();
        while (true){
            HelloRMIService helloRMIService=consumer.lookup();
            try {
                String result = helloRMIService.sayHello("wangyj");
                System.out.println(result);
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
