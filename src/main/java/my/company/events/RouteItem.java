package my.company.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Created by christoph on 02.02.15.
 */

public class RouteItem {
    private final AjaxRequestTarget target;

    /**
     * Constructor
     *
     * @param target
     */
    public RouteItem(AjaxRequestTarget target) {
        this.target = target;
    }

    /**
     * @return ajax request target
     */
    public AjaxRequestTarget getTarget() {
        return target;
    }

}