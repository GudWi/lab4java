package hzl;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.enterprise.inject.Produces;

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
