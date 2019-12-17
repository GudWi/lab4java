package hzl;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class HazelcastProducer {
    @Inject
    private Config config;

    private HazelcastInstance hazelcastInstance = null;

    @Produces
    public HazelcastInstance getHazelcast(){
        if(hazelcastInstance == null){
            this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        }

        return this.hazelcastInstance;
    }
}
