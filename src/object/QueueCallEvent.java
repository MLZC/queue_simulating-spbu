package object;

import queuesimulation.Model_3;

/**
 * The event of job's arrival.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 02.04.2018 20:05:08
 * Corrected 29.11.2018
 */
public class QueueCallEvent extends QueueEvent {

    private final JobToServe job;

    public QueueCallEvent(double time, JobToServe job) {
        super(time);
        this.job = job;
    }

    /**
     * Get the job (call, request) of the event.
     * @return Job (call, request) of the event.
     */
    public JobToServe getJob() {
        return job;
    }

    @Override
    public void processEvent(Model_3 model) {
//        System.out.println("in " + getTime() + "; free: " + model.freeUnitsNumber + ";  queue: " + model.queue.size());
        model.processCallEvent(this);
    }


}
