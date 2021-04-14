package object;


import java.util.ArrayList;

/**
 * The waiting room model.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 10.04.2013 17:45:35
 * Corrected 29.11.2018
 */
public class Waiting {

    /** The notation of unlimited waiting room. */
    public static final int UNLIMITED = -1;

    /** Maximal length of jobs queue. */
    public int capacity = 0;

    /** Maximal waiting time in queue. */
    public int duration;

    /** Queue of jobs. */
    private ArrayList<JobToServe> jobs = new ArrayList<>();

    /** Time moment of last event when the length of queue was changed.*/
    private double lastLengthChangeTime = 0;

    /** Integrated jobs' queue with respect to time.*/
    private double filling = 0;

    /** Average length of queue (average number of jobs in the waiting room).*/
    private double averageLength = 0;

    /** Maximal length of queue (maximal number of jobs in the waiting room).*/
    private double maxLength = 0;

    /**
     * Adds a job to the queue.
     * @param job the job to be added to the end of the queue
     * @param curTime current time
     * @return
     */
    public boolean add(JobToServe job, double curTime){
        if((capacity == UNLIMITED || jobs.size() < capacity) && (job.type > 0)) {
            if(!jobs.add(job)) {
                throw new RuntimeException("Job instance can't be added to the queue");
            }
            if(jobs.size() > maxLength) maxLength = jobs.size();
            job.startWaitTime = curTime;
            filling += (jobs.size() - 1) * (curTime - lastLengthChangeTime);
            averageLength = filling / curTime;
            lastLengthChangeTime = curTime;
            return true;
        } else {
            return false;
        }
    }


    /**
     * Removes the job from the queue.
     * @param i number of jobs in queue
     * @param curTime current time
     * @return removed object
     */
    public JobToServe remove(int i, double curTime) {
        JobToServe job = jobs.get(i);
        job.finishWaitTime = curTime;
        jobs.remove(i);
        filling += (jobs.size() + 1) * (curTime - lastLengthChangeTime);
        averageLength = filling / curTime;
        lastLengthChangeTime = curTime;
        return job;
    }

    /**
     * Returns the number of jobs in queue at the currrenr time.
     * @return queue length
     */
    public int size(){
        return jobs.size();
    }

    /**
     * Returns a job by the number in queue.
     * @param i number of a job in queue
     * @return job number i in queue
     */
    public JobToServe get(int i) {
        return jobs.get(i);
    }

    /**
     * Cleans waiting room from all jobs.
     */
    public void clear(){
        lastLengthChangeTime = 0;
        filling = 0;
        averageLength = 0;
        maxLength = 0;
        jobs.clear();
    }

    /**
     * Returns average number of jobs in waiting room.
     * @return average queue length
     */
    public double getAvarageLength() {
        return averageLength;
    }

    /**
     * Returns maximal number of jobs in waiting room.
     * @return maximal queue length
     */
    public double getMaxLength() {
        return maxLength;
    }

}
