package businessmq.reg.listener;

import businessmq.log.MqLogManager;
import businessmq.reg.base.AbstractConnectListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/1/23.
 */
public class ConnectListener extends AbstractConnectListener {
    @Override
    public void changed(CuratorFramework curatorFramework, ConnectionState connectionState) {
        MqLogManager.log("System","与zookeeper连接状态发生变化:【"+connectionState+"】",new Date());
    }
}
