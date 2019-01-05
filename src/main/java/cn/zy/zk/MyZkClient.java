package cn.zy.zk;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

/**
 * @Author: zhangying
 * @Date: 2019/1/4 18:19
 * @Version 1.0
 */
public class MyZkClient {
    private static  final String connectString="hadoop101:2181,hadoop102:2181,hadoop103:2181"; //不要有空格
    private static  final int sessionTimeout=2000;
    private ZooKeeper zkc;//客户端



    @Before
    public void init() throws IOException {
        zkc=new ZooKeeper(connectString, sessionTimeout, new Watcher(  ) {
            /*
             * new ZooKeeper()会开启2个线程，一个负责通信，一个负责监听
             * */

            //监控节点变化
            public void process(WatchedEvent watchedEvent) {
                System.out.println("---------start----------");
                List<String > children= null;
                try {
                    //1)监控哪个目录，2）是否监控
                    children = zkc.getChildren("/",true);
                    for(String child : children){
                        System.out.println(child);
                    }
                    System.out.println("---------end----------");

                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Test//创建目录节点
    public  void  createNode() throws KeeperException, InterruptedException {

        //1)路径；2）data,必须2进制；3)访问权限控制；4)创建节点种类
        String path=zkc.create("/xuexiao","cjdx".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        System.out.println(path);
    }



    //获取子节点并监控节点变化
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {
        //第二个参数，要不要监听
        List<String> children = zkc.getChildren("/",false);

        for (String child : children) {
            System.out.println(child);
        }

    }

    @Test//判断节点是否存在
    public void exist() throws KeeperException, InterruptedException {
        //1路径，2是否监听
        Stat stat= zkc.exists("/",false);//如果不存在节点，返回null
        System.out.println(stat==null ? "cunzai":"bucunzai");
    }
}