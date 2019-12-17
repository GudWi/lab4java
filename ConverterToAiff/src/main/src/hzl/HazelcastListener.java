package hzl;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.listener.EntryAddedListener;
import convert.AudioConverter;
import models.AudioModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.File;

@Default
@ApplicationScoped
public class HazelcastListener implements EntryAddedListener<Long, AudioModel> {
    @Inject
    private HazelcastInstance hazelcastInstance;

    @Inject
    private AudioConverter converter;

    private FlakeIdGenerator idGenerator;

    public void init(){
        hazelcastInstance.getMap("wav").addEntryListener(this,true);
        idGenerator = hazelcastInstance.getFlakeIdGenerator("convertedToAiff");
    }

    @Override
    public void entryAdded(EntryEvent<Long, AudioModel> entryEvent) {
        AudioModel model = entryEvent.getValue();
        File convertedFile = converter.ConvertFileToAIFF(model.getAudio());

        if (convertedFile != null){
            IMap<Object, Object> map = hazelcastInstance.getMap("convertedToAiff");
            model.setAudio(convertedFile);
            map.put(idGenerator.newId(), model);
        } else {
            IMap<Object, Object> map = hazelcastInstance.getMap("failure");
            map.put(idGenerator.newId(), model);
        }
    }
}
