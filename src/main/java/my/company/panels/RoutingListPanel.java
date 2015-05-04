package my.company.panels;


import com.google.common.collect.Lists;
import my.company.domain.Location;
import my.company.domain.Route;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;


public class RoutingListPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public RoutingListPanel(String id, final IModel<List<Route>> routingModel) {
        super(id);


        ListView<Route> routeListView = new ListView<Route>("routes", routingModel.getObject()) {

            @Override
            protected void populateItem(ListItem<Route> listItem) {
                final IModel<Route> routeModel = listItem.getModel();
                listItem.add(new Label("routeName", routeModel.getObject().getName()));
                listItem.add(new ListView<Location>("routingItems", routeModel.getObject().getLocations()) {

                    @Override
                    protected void populateItem(ListItem<Location> listItem) {
                        final IModel<Location> locationModel = listItem.getModel();
                        listItem.add(new Label("locationName", locationModel));
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


