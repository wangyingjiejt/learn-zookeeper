package zk.constant;

public interface Constant {

    String ZK_PATH="localhost:2181";

    int ZK_SESSION_TIMEOUT =5000;

    String ZK_REGISTRY_PATH="/registry";

    String ZK_PROVIER_PATH=ZK_REGISTRY_PATH+"/provider";
}
