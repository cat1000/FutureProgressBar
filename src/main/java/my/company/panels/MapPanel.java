package my.company.panels;

import com.google.common.collect.Lists;
import my.company.FutureUpdateBehaviour;
import my.company.service.RouteGenerateService;
import my.company.domain.Route;
import my.company.events.RouteItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.util.List;
import java.util.concurrent.Future;


public class MapPanel extends Panel {

    private static final long serialVersionUID = 1L;


    private IModel<List<Route>> routingModel;

    private List<Route> routeList = Lists.newArrayList();

    private StringBuilder builder;

    Panel routingListDummyPanel;
    Panel routingListPanel;
    Panel progressBar;


    @SpringBean
    private RouteGenerateService routeGenerateService;


    public MapPanel(String id) {
        super(id);

        Route route = new Route("default route");
        routeList.add(route);


        // initialize a default model so that update of page is fast
        // real model is loaded via future
        routingModel = new LoadableDetachableModel<List<Route>>() {
            @Override
            protected List<Route> load() {
                return routeList;
            }

        };

        // create a dummy panel first that is non visible (act as a placeholder) which will be replaced by a wicket event through the other panel with the data
        routingListDummyPanel = new DummyPanel("routingPanel");
        routingListPanel = new RoutingListPanel("routingPanel", routingModel);

        if (routingListPanel.getDefaultModelObject() == null) {
            System.out.println("defaultmodel is null");
        }
        // todo here I do not understand why I have to set also the default model
        // todo when calling # routingListPanel.getDefaultModel then I always get NULL
        routingListPanel.setDefaultModel(routingModel);
        routingListDummyPanel.setVisible(false);
        add(routingListDummyPanel);

        //only show when loading the data from the route generate service
        progressBar = new ProgressbarPanel("progressBar");
        progressBar.setVisible(false);
        add(progressBar);

        this.setOutputMarkupId(true);

    }


    @Override
    public void renderHead(IHeaderResponse response) {

        // todo here I am initializing some JS code based on the routes so the mapPanel has to be reloaded as well
        builder = new StringBuilder();

        response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
    }


    private void addRoute(StringBuilder builder) {

        // just sample data to demonstrate functionality
        int i = 1;
        for (Route route : routingModel.getObject()) {
            builder.append(route.getName());

        }

    }


    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        if (event.getPayload() instanceof RouteItem) {

            RouteItem update = (RouteItem) event.getPayload();


            // replace routingListDummyPanel with real Panel
            if (routingListDummyPanel.getParent() != null) {
                routingListDummyPanel.replaceWith(routingListPanel);
            }

            // only execute future task (load route data when not having the dummy panel in place)
            if (routingListDummyPanel.getParent() == null) {


                final Future<List<Route>> contentsFuture = routeGenerateService.generateRoutes();

                FutureUpdateBehaviour<List<Route>> futureUpdateBehaviour = new FutureUpdateBehaviour<List<Route>>(Duration.seconds(2), contentsFuture) {
                    @Override
                    protected void onPostSuccess(AjaxRequestTarget target, List<Route> routes) {

                        System.out.println("Success...loading data finished");
                        System.out.println("Reloading panel");

                        //todo needed to set the list for the LDM??
                        routeList = routes;

                        // the whole mapPanel should be updated (in order to also reload the JS in the renderHead section)
                        // the progressBar should be hidden
                        target.add(routingListPanel.getParent());


                    }

                    @Override
                    protected void onUpdateError(AjaxRequestTarget target, Exception e) {

                    }
                };

                routingListPanel.add(futureUpdateBehaviour);

            }


            routingListPanel.setOutputMarkupId(true);
            routingListPanel.setVisible(true);

            update.getTarget().add(this);

        }



    }


}

