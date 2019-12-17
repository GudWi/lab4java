package hzl;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class HazelcastConfig {
    @Produces
    public Config config(){
        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(
                        new MapConfig()
                                .setName("configuration")
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(-1));
        return config;
    }
}
