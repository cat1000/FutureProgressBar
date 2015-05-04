package my.company;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by christoph on 04.05.15.
 */
public abstract class FutureUpdateBehaviour<T> extends AbstractAjaxTimerBehavior {
    private final Logger logger = LoggerFactory.getLogger(FutureUpdateBehaviour.class);
    private transient Future<T> future;

    public FutureUpdateBehaviour(Duration updateInterval, Future<T> future) {
        super(updateInterval);
        this.future = future;
    }

    protected abstract void onPostSuccess(AjaxRequestTarget target, T data);

    protected abstract void onUpdateError(AjaxRequestTarget target, Exception e);

    protected void onTimer(final AjaxRequestTarget target){
        if(future.isDone()){
            try{
                final T data = future.get();

                getComponent().setDefaultModelObject(data);
                stop(target);
                onPostSuccess(target, data);
            }
            catch(InterruptedException e){
                stop(target);
                String message = "Error occurred while fetching data: "+e.getMessage();
                logger.error(message, e);
                onUpdateError(target, e);
            }
            catch(ExecutionException e){
                stop(target);
                String message = "Error occurred while fetching data: "+e.getMessage();
                logger.error(message, e);
                onUpdateError(target, e);
            }
        }
    }
}