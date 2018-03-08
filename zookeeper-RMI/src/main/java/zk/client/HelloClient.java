package zk.client;

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
    public static void main(String[] args) {
        try {
            HelloRMIService hello = (HelloRMIService) Naming.lookup("rmi://localhost:1099/hello");
            while (true) {
                System.out.println(hello.sayHello("wangyj"));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}