package object;

/**
 * The model of Job to serve.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 04.04.2013 21:50:32
 * Corrected 29.11.2018
 */
public class JobToServe {

    /** Arrival time relative to last job arrival. */
    public double time;

    /** Type of job (does not used). */
    public int type;

    /** Amount of job (here the service time). */
    public double amount;

    /** Time moment when the job was queued. */
    public double startWaitTime;

    /** Time moment when the job was taken to serve after waiting in queue. */
    public double finishWaitTime;


    public double getWaitingTime(){
        return finishWaitTime - startWaitTime;
    }

    public double getServiceTime() {
        return amount;
    }
}
