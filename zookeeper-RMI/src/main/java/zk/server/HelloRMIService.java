package zk.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 定义接口及抽方法
 * @user wangyj
 * @date 2018/3/8 10:56
 */
public interface HelloRMIService extends Remote {
    public String sayHello(String name)throws RemoteException;

}
