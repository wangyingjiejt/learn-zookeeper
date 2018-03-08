package zk.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 实现定义的远程接口
 * @user wangyj
 * @date 2018/3/8 10:57
 */
public class HelloRMIServiceImpl  extends UnicastRemoteObject implements HelloRMIService{

    //这个实现必须要有一个显式的构造函数，且要抛出RemoteException异常
    public HelloRMIServiceImpl() throws RemoteException{super();};

    public String sayHello(String name) throws RemoteException {
        System.out.println("sayHello was called once ...");
        return "Hello,"+name;
    }


}
