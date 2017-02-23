package businessmq.reg.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * Created by alan.zheng on 2017/1/23.
 */
public abstract class AbstractConnectListener implements ConnectionStateListener {
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        changed(curatorFramework,connectionState);
    }

    public abstract void changed(final CuratorFramework curatorFramework, final ConnectionState connectionState);
}
