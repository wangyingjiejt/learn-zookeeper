package zk.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 实现sayHello方法
 * @user wangyj
 * @date 2018/3/8 10:57
 */
public class HelloRMIServiceImpl  extends UnicastRemoteObject implements HelloRMIService{

    private Logger logger= LoggerFactory.getLogger(HelloRMIServiceImpl.class);

    //这个实现必须要有一个显式的构造函数，且要抛出RemoteException异常
    public HelloRMIServiceImpl() throws RemoteException{super();};

    public String sayHello(String name) throws RemoteException {
        logger.info("sayHello was called once ...");
        return "Hello,"+name;
    }


}
