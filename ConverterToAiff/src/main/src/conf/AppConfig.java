package conf;

import com.hazelcast.core.HazelcastInstance;
import hzl.HazelcastListener;
import org.jboss.resteasy.cdi.ResteasyCdiExtension;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/app")
public class AppConfig extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    @Inject
    private HazelcastInstance hazelcastInstance;

    @Inject
    private HazelcastListener listener;

    @Inject
    private ResteasyCdiExtension extension;

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> result = new HashSet<>();
        //result.add(ReceiveServlet.class);
        result.addAll((Collection<? extends Class<?>>) (Object)extension.getResources());
        listener.init();

        return result;
    }
}