package my.company.domain;

import my.company.domain.Location;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Route implements Serializable {


    private String name;
    private final LinkedList<Location> locationList = new LinkedList<Location>();

    public Route(String name) {
        this.name = name;
    }

    public void addLocation(Location location) {
        this.locationList.add(location);
    }

    public LinkedList<Location> getLocations() {
        return locationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
