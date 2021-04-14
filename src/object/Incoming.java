package object;

import random.RandomSequence;

/**
 * The model of arrival stream og jobs
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 02.04.2013 23:13:33
 * Corrected 29.11.2018
 */
public class Incoming {

    /** The sequence of time intervals between jobs' arrivals. */
    public RandomSequence timeIntervals;
    /**
     * The job amount, for example it may be the time needed to serde it.
     */
    public RandomSequence jobAmounts;

    /**
     * The probability that the job agree to wait if all servers are busy.
     */
    public double agreeToWaitProbability = 1;

    /**
     * Returns the next arriving job.
     * @return a job.
     */
    public JobToServe nextCall(){
        JobToServe job = new JobToServe();
        job.time = timeIntervals.getNext();
        job.amount = jobAmounts.getNext();
        job.type = Math.random() > agreeToWaitProbability ? 0 : 1;
        return job;
    };

}
