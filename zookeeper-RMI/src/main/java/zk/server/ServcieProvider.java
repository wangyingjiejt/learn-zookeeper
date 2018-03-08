package zk.server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.constant.Constant;


import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * RMI向zk注册发布服务
 * @user wangyj
 * @date 2018/3/8 16:11
 */
public class ServcieProvider {

    private Logger logger= LoggerFactory.getLogger(PolicyUtils.ServiceProvider.class);

    private CountDownLatch latch = new CountDownLatch(1);


    //发布RMI服务并注RMI地址到zk中
    public void publish(Remote remote,String host ,int port){
        String url = publishService(remote,host,port);
        if(url!=null){
            ZooKeeper zk = connectServer();
            if (zk!=null){
                createNode(zk,url);
            }
        }
    }



    //发布服务并返回返回RMI地址
    private String publishService(Remote remote, String host, int port) {
        String url = null;
        try {
            url= String.format("rmi://%s:%d/%s",host,port,remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url,remote);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    //在zk上创建一个zk节点
    private void createNode(ZooKeeper zk, String url) {
        try {
            byte[] data = url.getBytes();
            //创建一个临时有序节点 存放的数据为该server在RMI中注册的url  OPEN_ACL_UNSAFE--不设置访问权限
            String path = zk.create(Constant.ZK_PROVIER_PATH,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper node ({}->>{})",path,url);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //连接zk
    private ZooKeeper connectServer() {
        ZooKeeper zk =null;
        try {
            zk = new ZooKeeper(Constant.ZK_PATH, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState()==Event.KeeperState.SyncConnected){
                        latch.countDown();
                    }
                }
            });
            latch.await(); //当latch中的计数器为0之前，程序会阻塞在这里（即只有与zk建立了连接才能返回zk）
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
