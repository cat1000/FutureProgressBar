package my.company.panels;


import my.company.events.RouteItem;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class MenuPanel extends Panel {

    private static final long serialVersionUID = 1L;


    public MenuPanel(String id) {
        super(id);

        WebMarkupContainer generateRoute = new WebMarkupContainer("generateRoute");
        add(generateRoute);

        generateRoute.add(new AjaxEventBehavior("onclick") {
            protected void onEvent(AjaxRequestTarget target) {

                System.out.println("button clicked, sending event");

                send(getPage(), Broadcast.BREADTH, new RouteItem(target));

            }

        });




    }

}
