package my.company.service;

import com.google.common.collect.Lists;
import my.company.domain.Location;
import my.company.domain.Route;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service("routeGenerateService")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RouteGenerateService {

    private final ExecutorService pool = Executors.newFixedThreadPool(10);


    // this parameter is used to deliver progress for the progressBar
    private int progress = 0;


    @Deprecated
    public RouteGenerateService() {
    }


    public Future<List<Route>> generateRoutes() {

        return pool.submit(new Callable<List<Route>>() {

            @Override
            public List<Route> call() {
                System.out.println("Long running task started");
                int i = 1;
                while  (i <= 5) {
                    progress += 20;
                    System.out.println("progress " + progress);

                    try {
                        Thread.sleep(1000);     // sleep one second (in the real program I would make a thread that tries to find best route and then stops after 10 sec).
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i++;

                }

                Route route = new Route("route 1");
                route.addLocation(new Location("location1"));
                route.addLocation(new Location("location2"));
                route.addLocation(new Location("location3"));

                List<Route> bestRoutes = Lists.newArrayList();
                bestRoutes.add(route);


                return bestRoutes;

            }
        });


    }

    public int getProgress() {
        return progress;
    }
}



