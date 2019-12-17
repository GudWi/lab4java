package resource;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import models.AudioModel;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Path("/receive")
public class ReceiveServlet {
    @Inject
    private HazelcastInstance hazelcast;

    private FlakeIdGenerator aiffIdGenerator;

    private FlakeIdGenerator wavIdGenerator;

    @PostConstruct
    private void init(){
        aiffIdGenerator = hazelcast.getFlakeIdGenerator("aiffFromRest");
        wavIdGenerator = hazelcast.getFlakeIdGenerator("wavFromRest");
    }

    @POST
    @Consumes("audio/wav")
    @Path("/postAiffFile")
    public String postAiffFile(File file){
        IMap<Object, Object> aiffFiles = hazelcast.getMap("wav");

        Long id = aiffIdGenerator.newId();
        AudioModel model = new AudioModel();

        JSONObject result = new JSONObject();

        model.setAudio(file);
        model.setId(id);

        aiffFiles.put(id, model);

        boolean find = false;

        try {
            while (!find) {
                IMap<Object, Object> map = hazelcast.getMap("convertedToAiff");
                IMap<Object, Object> failMap = hazelcast.getMap("failure");

                for(Map.Entry<Object, Object> entry : map.entrySet()){
                    if(((AudioModel)entry.getValue()).getId().equals(id)){
                        find = true;

                        result.put("success", true);
                        result.put("id", entry.getKey().toString());

                        return result.toJSONString();
                    }
                }

                for(Map.Entry<Object, Object> entry : failMap.entrySet()){
                    if(((AudioModel)entry.getValue()).getId().equals(id)){
                        find = true;
                        result.put("success", false);
                        result.put("id", entry.getKey().toString());

                        return result.toJSONString();
                    }
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            System.out.println("InterruptedException");
        }

        result.put("success", false);
        result.put("id", id);

        return result.toJSONString();
    }

    @POST
    @Consumes("audio/aiff")
    @Path("/postWaveFile")
    public String postWavFile(File file){
        IMap<Object, Object> wavFiles = hazelcast.getMap("aiff");

        Long id = wavIdGenerator.newId();
        AudioModel model = new AudioModel();

        model.setAudio(file);
        model.setId(id);

        JSONObject result = new JSONObject();

        wavFiles.put(id, model);

        boolean find = false;

        try {
            while (!find) {
                IMap<Object, Object> map = hazelcast.getMap("convertedToWav");
                IMap<Object, Object> failMap = hazelcast.getMap("failure");

                for(Map.Entry<Object, Object> entry : map.entrySet()){
                    if(((AudioModel)entry.getValue()).getId().equals(id)){
                        find = true;

                        result.put("success", true);
                        result.put("id", entry.getKey().toString());

                        return result.toJSONString();
                    }
                }

                for(Map.Entry<Object, Object> entry : failMap.entrySet()){
                    if(((AudioModel)entry.getValue()).getId().equals(id)){
                        find = true;

                        result.put("success", false);
                        result.put("id", entry.getKey().toString());

                        return result.toJSONString();
                    }
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            System.out.println("InterruptedException");
        }

        result.put("success", false);
        result.put("id", id);

        return result.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAiffFile/{id}")
    public String getAiffFile(@PathParam("id") Long id){
        IMap<Object, Object> map = hazelcast.getMap("convertedToAiff");

        try {
            AudioModel model = (AudioModel) map.get(id);

            if(model != null){
                JSONObject result = new JSONObject();

                try {
                    byte[] b = Files.readAllBytes(model.getAudio().toPath());
                    String base64String = Base64.encodeBase64String(b);
                    result.appendField("name", model.getAudio().getName());
                    result.appendField("bytes", base64String);

                    return result.toJSONString();
                } catch (IOException e){
                    return "";
                }
            } else {
                return "";
            }
        } catch (ClassCastException e){
            return "";
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getWaveFile/{id}")
    public String getWavFile(@PathParam("id") Long id){
        IMap<Object, Object> map = hazelcast.getMap("convertedToWav");

        try {
            AudioModel model = (AudioModel) map.get(id);

            JSONObject result = new JSONObject();

            try {
                byte[] b = Files.readAllBytes(model.getAudio().toPath());
                String base64String = Base64.encodeBase64String(b);
                result.appendField("name", model.getAudio().getName());
                result.appendField("bytes", base64String);

                return result.toJSONString();
            } catch (IOException e){
                return "";
            }
        } catch (ClassCastException e){
            return "";
        }
    }
}
