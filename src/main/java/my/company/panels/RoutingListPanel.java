package my.company.panels;


import java.util.List;

import my.company.domain.Location;
import my.company.domain.Route;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;


public class RoutingListPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public RoutingListPanel(String id, final IModel<List<Route>> routingModel) {
        super(id);


        ListView<Route> routeListView = new PropertyListView<Route>("routes", routingModel) {

			private static final long serialVersionUID = 1L;

			@Override
            protected void populateItem(ListItem<Route> listItem) {
                
                listItem.add(new Label("name"));
                listItem.add(new PropertyListView<Location>("locations") {

					private static final long serialVersionUID = 1L;

					@Override
                    protected void populateItem(ListItem<Location> listItem) {
                        listItem.add(new Label("name"));
                        listItem.setOutputMarkupId(true);

                    }
                });
                listItem.setOutputMarkupId(true);

                this.setOutputMarkupId(true);
            }


        };

        add(routeListView);

        this.setOutputMarkupId(true);


    }

}


