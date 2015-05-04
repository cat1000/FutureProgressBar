package my.company.pages;

import my.company.panels.MapPanel;
import my.company.panels.MenuPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class TestPage extends WebPage {
    private static final long serialVersionUID = 1L;

    // Model //
    private final Model<Integer> model = Model.of(10);

    public TestPage() {
        this.initialize();
    }

    private void initialize() {

        Panel mapPanel = new MapPanel("mapPanel");
        add(mapPanel);


        Panel menuPanel = new MenuPanel("menuPanel");
        add(menuPanel);

    }



}