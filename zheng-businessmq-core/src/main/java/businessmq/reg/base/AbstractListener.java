package businessmq.reg.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * zk监听抽象类
 * Created by XR on 2016/12/21.
 */
public abstract class AbstractListener implements TreeCacheListener {
    public final void childEvent(final CuratorFramework curatorFramework, final TreeCacheEvent treeCacheEvent) throws Exception {
        changed(curatorFramework,treeCacheEvent);
    }

    public abstract void changed(final CuratorFramework curatorFramework, final TreeCacheEvent treeCacheEvent);
}
