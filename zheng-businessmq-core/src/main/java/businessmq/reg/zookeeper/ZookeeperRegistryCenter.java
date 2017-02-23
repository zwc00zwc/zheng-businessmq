package businessmq.reg.zookeeper;

import businessmq.log.MqLogManager;
import businessmq.reg.base.RegistryCenter;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * zk 连接注册实现类
 * Created by XR on 2016/12/21.
 */
public class ZookeeperRegistryCenter implements RegistryCenter {
    /**
     * zk config
     */
    private ZookeeperConfig zkConfig;
    /**
     * zk caches
     */
    private final Map<String, TreeCache> caches = new HashMap<String, TreeCache>();

    public ZookeeperRegistryCenter(ZookeeperConfig zookeeperConfig){
        zkConfig=zookeeperConfig;
    }
    /**
     * zk client
     */
    private CuratorFramework client;

    public void init(){
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getServerLists())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMilliseconds(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepTimeMilliseconds()))
                .namespace(zkConfig.getNamespace());
        if (0 != zkConfig.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeoutMilliseconds());
        }
        if (0 != zkConfig.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(zkConfig.getConnectionTimeoutMilliseconds());
        }
        if (!Strings.isNullOrEmpty(zkConfig.getAuth())) {
            builder.authorization("digest", zkConfig.getAuth().getBytes(Charsets.UTF_8))
                    .aclProvider(new ACLProvider() {
                        private List<ACL> acl ;

                        public List<ACL> getDefaultAcl() {
                            if(acl ==null){
                                ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                                acl.clear();
                                acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", zkConfig.getAuth()) ));
                                this.acl = acl;
                            }
                            return acl;
                        }

                        public List<ACL> getAclForPath(final String path) {
                            return acl;
                        }
                    });
        }
        client = builder.build();
        client.start();
        try {
            if (!client.blockUntilConnected(zkConfig.getMaxSleepTimeMilliseconds() * zkConfig.getMaxRetries(), TimeUnit.MILLISECONDS)) {
                client.close();
                throw new KeeperException.OperationTimeoutException();
            }
        } catch (InterruptedException e) {
            MqLogManager.log("System",e.toString(),new Date());
        } catch (KeeperException.OperationTimeoutException e) {
            MqLogManager.log("System",e.toString(),new Date());
        }
//        String connectString = zkConfig.getServerLists();
//        // 连接时间 和重试次数
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
//
//        client.start();
    }

    public void close() {
        for (Map.Entry<String, TreeCache> each : caches.entrySet()) {
            each.getValue().close();
        }
        waitForCacheClose();
        CloseableUtils.closeQuietly(client);
    }

    /*  等待500ms, cache先关闭再关闭client, 否则会抛异常
     * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
     * 等待Curator新版本解决这个bug.
     * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
     */
    private void waitForCacheClose() {
        try {
            Thread.sleep(500L);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public String get(final String key) {
        TreeCache cache = findTreeCache(key);
        if (null == cache) {
            return getDirectly(key);
        }
        ChildData resultInCache = cache.getCurrentData(key);
        if (null != resultInCache) {
            return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charsets.UTF_8);
        }
        return getDirectly(key);
    }

    private TreeCache findTreeCache(final String key) {
        for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
            if (key.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String getDirectly(final String key) {
        try {
            return new String(client.getData().forPath(key), Charsets.UTF_8);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
            return null;
        }
    }

    public List<String> getChildrenKeys(final String key) {
        try {
            List<String> result = client.getChildren().forPath(key);
            Collections.sort(result, new Comparator<String>() {

                public int compare(final String o1, final String o2) {
                    return o2.compareTo(o1);
                }
            });
            return result;
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
            return Collections.emptyList();
        }
    }

    public int getNumChildren(final String key) {
        try {
            Stat stat = client.getZookeeperClient().getZooKeeper().exists(getNameSpace() + key, false);
            if (null != stat) {
                return stat.getNumChildren();
            }
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
        return 0;
    }

    private String getNameSpace() {
        String result = this.zkConfig.getNamespace();
        return Strings.isNullOrEmpty(result) ? "" : "/" + result;
    }

    public boolean isExisted(final String key) {
        try {
            return null != client.checkExists().forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
            return false;
        }
    }

    public void create(final String key, final String value) {
        try {
            if (!isExisted(key)) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charsets.UTF_8));
            } else {
                update(key, value);
            }
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
    }

    public void update(final String key, final String value) {
        try {
            client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charsets.UTF_8)).and().commit();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
    }

    public void createEphemeral(final String key, final String value) {
        try {
            if (isExisted(key)) {
                client.delete().deletingChildrenIfNeeded().forPath(key);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
    }

    public String createSequential(final String key, final String value) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(key, value.getBytes(Charsets.UTF_8));
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
        return null;
    }

    public void createEphemeralSequential(final String key) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
    }

    public void remove(final String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
    }

    public long getRegistryCenterTime(final String key) {
        long result = 0L;
        try {
            String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key);
            result = client.checkExists().forPath(path).getCtime();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());
        }
        Preconditions.checkState(0L != result, "Cannot get registry center time.");
        return result;
    }

    public Object getRawClient() {
        return client;
    }

    public void addCacheData(final String cachePath) {
        TreeCache cache = new TreeCache(client, cachePath);
        try {
            cache.start();
            //CHECKSTYLE:OFF
        } catch (final Exception ex) {
            MqLogManager.log("System",ex.toString(),new Date());

        }
        caches.put(cachePath + "/", cache);
    }

    public Object getRawCache(final String cachePath) {
        return caches.get(cachePath + "/");
    }
}
