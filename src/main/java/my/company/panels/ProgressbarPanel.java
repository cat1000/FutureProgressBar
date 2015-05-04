package my.company.panels;

import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.progressbar.ProgressBar;
import my.company.events.RouteItem;
import my.company.service.RouteGenerateService;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

public class ProgressbarPanel extends Panel implements IAjaxIndicatorAware {

    private static final long serialVersionUID = 1L;

    private final ProgressBar progressBar;
    private final Form<Void> form;
    private final AbstractAjaxTimerBehavior timer;
    private final AjaxIndicatorAppender indicator = new AjaxIndicatorAppender();

    @SpringBean
    private RouteGenerateService routeGenerateService;


    private int counter = 0;


    public ProgressbarPanel(String id) {
        super(id);

        form = new Form<Void>("form");
        this.add(form);

        // FeedbackPanel //
        final FeedbackPanel feedback = new JQueryFeedbackPanel("feedback");
        form.add(feedback.setOutputMarkupId(true));

        // Timer //
        timer = new AbstractAjaxTimerBehavior(Duration.ONE_SECOND) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                int progress = routeGenerateService.getProgress();
                progressBar.forward(target, progress - counter);
                counter = progress;
            }
        };
        timer.stop(null);


        // ProgressBar //
        this.progressBar = new ProgressBar("progress", Model.of(counter)) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onValueChanged(AjaxRequestTarget target) {
                info("value: " + this.getDefaultModelObjectAsString());
                target.add(feedback);
            }

            @Override
            protected void onComplete(AjaxRequestTarget target) {
                timer.stop(target); //wicket6

                form.remove(timer);
                counter = 0;

                info("completed!");
                target.add(feedback);
            }
        };

        form.add(this.progressBar);

        // Indicator //
        form.add(new EmptyPanel("indicator").add(this.indicator));

        // Initialize FeedbackPanel //
        this.info("value: " + this.progressBar.getDefaultModelObjectAsString());

        this.setOutputMarkupId(true);
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return this.indicator.getMarkupId();
    }


    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        // receive information about the route generate event
        // add timer and display progress bar
        if (event.getPayload() instanceof RouteItem) {
            RouteItem update = (RouteItem) event.getPayload();
            form.add(timer);
            timer.restart(update.getTarget());
            form.getParent().setVisible(true);
            update.getTarget().add(form);

        }

    }

}


