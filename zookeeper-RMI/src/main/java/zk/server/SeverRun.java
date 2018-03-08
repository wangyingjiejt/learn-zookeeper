package zk.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * server启动
 * @user wangyj
 * @date 2018/3/8 10:58
 */
public class SeverRun {

    public static void main(String[] args) {

        try {
            HelloRMIService hello =new HelloRMIServiceImpl();
            //通过该方法仔仔JNDI中创建一个注册表，只需提供一个端口号即可
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/hello",hello);
            System.out.println("Hello service binding...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
