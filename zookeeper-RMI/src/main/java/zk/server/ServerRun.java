package zk.server;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * server启动
 * @user wangyj
 * @date 2018/3/8 10:58
 */
public class ServerRun {

    public static Logger logger= LoggerFactory.getLogger(ServerRun.class);
    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("config/log4j.properties");
        String host="localhost";
        int port=3099;//启动多个server需要修改端口号
        ServcieProvider provider = new ServcieProvider();
        HelloRMIService helloRMIService= new HelloRMIServiceImpl();
        provider.publish(helloRMIService,host,port);
        Thread.sleep(Long.MAX_VALUE);


    }
}
