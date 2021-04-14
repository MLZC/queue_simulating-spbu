package object;

import queuesimulation.Model_3;

/**
 * The event of job's leaving the server when the service is complete.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 02.04.2018 20:16:28
 * Corrected 29.11.2018
 */
public class QueueFreeEvent extends QueueEvent {

    /** Server, that will be free after the event. */
    private final ServiceUnit server;

    /** Job, that is served. */
    private final JobToServe job;

    /**
     * Constructor
     * @param time
     * @param server
     */
    public QueueFreeEvent(double time, ServiceUnit server) {
        super(time);
        this.server = server;
        this.job = null;
    }

    /**
     * Constructor
     * @param time
     * @param job
     */
    public QueueFreeEvent(double time, JobToServe job) {
        super(time);
        this.server = null;
        this.job = job;
    }

    /**
     * Return the server, that will be free after the event.
     * @return server.
     */
    public ServiceUnit getUnit() {
        return server;
    }

    /**
     * Get job that service is finished.
     * @return Finished job.
     */
    public JobToServe getJob() {
        return job;
    }

    @Override
    public void processEvent(Model_3 model) {
        model.processFreeEvent(this);
    }

}
