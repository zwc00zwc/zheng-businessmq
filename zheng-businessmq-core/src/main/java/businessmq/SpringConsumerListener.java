package businessmq;

import businessmq.config.ConsumerConfig;
import businessmq.consumer.AbstractConsumer;
import businessmq.db.DbConfig;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Optional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by alan.zheng on 2017/2/10.
 */
public class SpringConsumerListener extends ConsumerListener {
    private final AbstractConsumer abstractConsumer;

    public SpringConsumerListener(final ConsumerConfig _config, DbConfig dbConfig, ZookeeperRegistryCenter zookeeperRegistryCenter, AbstractConsumer abstractConsumer, ThreadPoolTaskExecutor threadPoolTaskExecutor){
        super(_config,dbConfig,threadPoolTaskExecutor,zookeeperRegistryCenter);
        this.abstractConsumer=abstractConsumer;
    }

    @Override
    protected Optional<AbstractConsumer> createBaseJobInstance() {
        return Optional.fromNullable(abstractConsumer);
    }
}
