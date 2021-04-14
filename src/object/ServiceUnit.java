package object;

/**
 * The model of the server in queueing sysrtem.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 30.03.2015 15:44:59
 * Corrected 29.11.2018
 */
public class ServiceUnit {

    /** Time moment when this state (free or busy) was started. */
    public double lastChangeStateTime = 0;

    /** Sum of time while server is busy. */
    public double sumBusyTime = 0;

    /** Sum of time while server is free. */
    public double sumFreeTime = 0;

    /** Job in service now, null if no. */
    public JobToServe currentJob;

    /**
     * Set call (requst) in service.
     * @param job
     * @param curTime
     */
    public void setJob(JobToServe job, double curTime) {
        currentJob = job;
        sumFreeTime += curTime - lastChangeStateTime;
        lastChangeStateTime = curTime;
    }

    /**
     * Remove the job (call, request) from service unit.
     * @param curTime current time.
     * @return removed job
     */
    public JobToServe finJob(double curTime) {
        sumBusyTime += curTime - lastChangeStateTime;
        lastChangeStateTime = curTime;
        JobToServe ret = currentJob;
        currentJob = null;
        return ret;
    }

    /**
     * Set server to initial condition.
     * @param curTime Initial time.
     */
    public void init(double curTime) {
        lastChangeStateTime = curTime;
        sumBusyTime = 0;
        sumFreeTime = 0;
        currentJob = null;
    }

}
