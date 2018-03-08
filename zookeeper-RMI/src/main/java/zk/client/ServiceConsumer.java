package zk.client;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.constant.Constant;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 从zk获取RMI中发布的服务
 * @User wangyj
 * @Date 2018/3/8  下午10:54
 * @param urlList
 * @return
 */
public class ServiceConsumer {

    private Logger logger= LoggerFactory.getLogger(ServiceConsumer.class);

    private CountDownLatch latch= new CountDownLatch(1);

    private volatile List<String> urlList = new ArrayList<String>();

    //构造时连接zk
    public ServiceConsumer() {
        ZooKeeper zk = connectServer();
        if (zk!=null){
            watchNode(zk);
        }
    }


    //在RMI中获取service
    public <T extends Remote> T lookup(){
        T service =null;
        int size =urlList.size();
        if (size>0){
            String url;
            if (size==1){
                url=urlList.get(0);
                logger.info("using only url:{}",url);
            }else {
                url=urlList.get(ThreadLocalRandom.current().nextInt(size));
                logger.info("using random url:{}",url);
            }
            service=lookupService(url);
        }
        return service;
    }


    //在JNDI中查找RMI远程服务对象
    private <T> T lookupService(String url){
        T remote =null;
        try {
            remote=(T) Naming.lookup(url);
        } catch (Exception e) {
            logger.error("ConnectException -> url:{}",url);
            if (urlList.size()!=0){
                url=urlList.get(0);
                return lookupService(url);
            }
            e.printStackTrace();
        }
        return remote;
    }

    //监控/registry子节点的变化，即是否有server加入或者挂掉
    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
                        watchNode(zk);//当监听到子节点发生变化时，重新获取nodeList
                    }
                }
            });
            //用于存放/registry 子节点中的数据
            List<String > dataList = new ArrayList<String>();
            for (String node :nodeList){
                byte[] data = zk.getData(Constant.ZK_REGISTRY_PATH+"/"+node,false,null);
                dataList.add(new String (data));
            }
            logger.info("node data:{}",dataList);
            urlList=dataList;//试试更新最新RMI的地址
        } catch (Exception e) {
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
