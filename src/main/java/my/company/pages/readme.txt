Intro
--------------
The page <TestPage> consists of 2 panels:
- panel <mapPanel> shows in reality a map
- panel <menuBar> shows some buttons

Panel <mapPanel> has a further subPanel <routeListPanel>.
By default, the subPanel <routeListPanel> is an empty dummy panel (act as a placeholder) which is replaced by the routeListPanel as soon as
the button "click to generate route" is called from the menuBar.

On this event,
- a wicket event is triggered
- a future is started that calls a long running service to load the model data which is used by the <mapPanel> and the <routeListPanel>
- the progressBar is displayed that shows the progress of the long running service loading the data


When the future task is finished,
- the progressBar should hide
- the mapPanel (including the subPanel) should be refreshed


Open Questions
--------------
1) By using the future task, I have seen that the tomcat is not shutdown properly anymore. I have to kill it manually. Why does this happen?

2) MapPanel.java, line 65
   When creating #new RoutingListPanel("routingPanel", routingModel); and call after it #routingListPanel.getDefaultModelObject(),
   the defaultmodelobject is null. Why does this happen?

3) MapPanel.java, line 133
   Do I have to set here the list again or is it enough to set the defaultmodelobject in #FutureUpdateBehaviour

4) MapPanel.java, line 136
   The mapPanel is not refreshed / the new locations (from the routeGenerateService) are not shown
