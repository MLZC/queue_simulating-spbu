package queuesimulation;

import java.util.Arrays;
import java.util.PriorityQueue;

import object.*;
import random.ConstantSequence;
import random.ExponentialSequence;
import random.UniformSequence;


/**
 * Simulating of queuing system with waiting room
 * and Poisson arrival stream.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 02.04.2018 19:41:02
 * Corrected 30.11.2018
 */
public class Model_3 {

    /**
     * Sequence of events in queueing system.
     */
    private final PriorityQueue<QueueEvent> events;

    /**
     * Servers to serve the jobs.
     */
    private final ServiceUnit[] servers;

    /**
     * Queue of jobs that are waiting for service in the waiting room.
     */
    private final Waiting queue;

    /**
     * Arrival stream od jobs.
     */
    private final Incoming inc;

    /**
     * Total number of jobs in simulation experiment.
     */
    private final int n;

    /**
     * Number of servers.
     */
    private final int nu;

    /**
     * Job arrival frequency.
     */
    private final double lambda;

    /**
     * Average service time.
     */
    private final double srv;

    /**
     * Maximal possible queue length.
     */
    private final int waitingRoomSize;

    /**
     * Total time amounts of each number of free servers.
     * Or summary duration of each system condition.
     */
    private final double[] servFreeSumTime;

    /**
     * Count of system condition changing. [i][0] from (i+1) to i; [i][1] from i to (i+1)
     */
    private final int[][] changeConditionCount;

    /**
     * Simulation current time.
     */
    private double curTime;

    /**
     * Count of served jobs.
     */
    private int count;

    /**
     * Count of denied jobs.
     */
    private int rejectedCount;

    /** Суммарное время ожидания заявками обслуживания в очереди. */
    /**
     * Jobs' total waiting time in queue to be served.
     */
    private double sumWaitingTime;

    /**
     * Jobs'  total sojorn time in the system (waiting + service).
     */
    private double sumServiceTime;

    /**
     * Current number of free servers while simulation experiment.
     */
    private int freeServersNumber;

    /**
     * The time moment of last changing the number of free servers.
     */
    private double lastFreeServsNumberChangeTime;

    /**
     * Default constructor.
     */
    public Model_3() {
        this(10000000,   //Total number of jobs served while simulation.
                4,       //Number of servers.
                3.75,       //Job arrival freequency.
                0.61,      //Average service time.
                11,//Waiting.UNLIMITED, //10,     //Waiting room limit. //

                'E'    //Notation of service time distribution.        ( 'U', 'E','C' )
        );
    }
//    public Model_3() {
//        this(10000000,   //Total number of jobs served while simulation.
//                6,       //Number of servers.
//                9.16,       //Job arrival freequency.
//                0.72,      //Average service time.
//                8,//Waiting.UNLIMITED, //10,     //Waiting room limit. //
//
//                'E'    //Notation of service time distribution.        ( 'U', 'E','C' )
//        );
//    }

    /**
     * Constructor: the deviation of the uniform distribution for the service time
     * is supposed to be equals to a half of the average service time.
     *
     * @param n               Total number of jobs served while simulation.
     * @param nu              Number of servers.
     * @param lambda          Job arrival frequency (arrival rate).
     * @param srvTime         Average service time.
     * @param waitingRoomSize Maximal possible queue length.
     * @param seqType         Notation of service time distribution:
     *                        'U' --- uniform, 'E' --- exponential, 'C' --- constant.
     */
    public Model_3(int n, int nu, double lambda, double srvTime, int waitingRoomSize, char seqType) {
        this(n, nu, lambda, srvTime, waitingRoomSize, seqType, srvTime / 2);
    }

    /**
     * General constructor.
     *
     * @param n               Total number of jobs served while simulation.
     * @param nu              Number of servers.
     * @param lambda          Job arrival frequency (arrival rate).
     * @param srvTime         Average service time.
     * @param waitingRoomSize Maximal possible queue length.
     * @param seqType         Notation of service time distribution: 'U' --- uniform, 'E' --- exonential, 'C' --- constant.
     * @param deviation       Half of the segment of uniform distribution of the service time.
     */
    public Model_3(int n, int nu, double lambda, double srvTime, int waitingRoomSize, char seqType, double deviation) {
        this.n = n;
        this.nu = nu;
        this.lambda = lambda; //2.5;
        this.srv = srvTime;//2.5;
        this.waitingRoomSize = waitingRoomSize;

        //Arrival stream.
        inc = new Incoming();
        //Random eponential generator for time intervals between job arrivals.
        inc.timeIntervals = ExponentialSequence.createByMean(1. / lambda);
        //Random generator for service times.
        inc.jobAmounts =
                seqType == 'U' ? UniformSequence.createByMeanAndDeviation(srv, deviation)     //Uniform.
                        : seqType == 'E' ? ExponentialSequence.createByMean(srv)                        //Exponential.
                        : ConstantSequence.createByMean(srv);                                           //Constant.
        //Waiting room (jobs' queue).
        queue = new Waiting();
        queue.capacity = waitingRoomSize;
        //Array of all servers.
        servers = new ServiceUnit[nu];
        //Server instances creation.
        for (int i = 0; i < servers.length; i++) {
            servers[i] = new ServiceUnit();
        }
        //Counts of system condition changing.
        changeConditionCount = new int[nu][2];
        //Summary times of being system in each condition.
        servFreeSumTime = new double[nu + 1];
        //Container of all events, sorting in chronological order.
        events = new PriorityQueue<>();
    }

    /**
     * Process simulation.
     */
    public void go() {
        //Initial assignment.
        curTime = 0;
        count = 0;
        rejectedCount = 0;
        sumServiceTime = 0;
        sumWaitingTime = 0;
        queue.clear();
        events.clear();
        for (ServiceUnit server : servers) {
            server.init(curTime);
        }
        Arrays.fill(servFreeSumTime, 0);
        freeServersNumber = nu;
        lastFreeServsNumberChangeTime = 0;
        for (int[] changeConditionCoun : changeConditionCount) {
            Arrays.fill(changeConditionCoun, 0);
        }
        //Creation of the first event.
        makeNextCallEvent();
        //Processing of all events in chronological order,
        //while count of served jobs is less than specified value.
        while (events.size() > 0 && count < n) {
            QueueEvent ev = events.poll();
            ev.processEvent(this);
        }
        //Free time and busy time summation over all servers.
        double sumUnitsFreeTime = 0, sumUnitsBusyTime = 0;
        for (ServiceUnit serv : servers) {
            sumUnitsFreeTime += serv.sumFreeTime;
            sumUnitsBusyTime += serv.sumBusyTime;
        }
        //Average number of free servers calculation.
        double avFreeSevers = 0;
        for (int i = 1; i < servFreeSumTime.length; i++) {
            avFreeSevers += servFreeSumTime[i] * i / curTime;
        }
        System.out.println("----------------------------------------Initial data------------------------------------"
                + "\r\n number of served jobs: " + count
                + ";\r\n number of servers: " + nu
                + ";\r\n job arrival frequency: " + lambda
                + ";\r\n average service time: " + srv
                + ";\r\n queue length limit: " + (waitingRoomSize == Waiting.UNLIMITED ? " ∞ " : waitingRoomSize)
                + ".\r\n--------------------------------------------Resulting data----------------------------------"
                + " \r\n total simulation time: " + curTime
                + ";\r\n average service time: " + sumServiceTime / count
                + ", together with denied jobs: " + sumServiceTime / (count + rejectedCount)
                + ";\r\n time in system --- average:  " + (sumWaitingTime + sumServiceTime) / (count + rejectedCount)
                + ", only for served jobs: " + (sumWaitingTime + sumServiceTime) / count
                + ";\r\n waiting time --- average: " + (sumWaitingTime / (count + rejectedCount))
                + ", average for served jobs: " + (sumWaitingTime / count)
                + ";\r\n queue length ---  average: " + queue.getAvarageLength()
                + ", maximal: " + queue.getMaxLength()
                + ", final: " + queue.size()
                + ";\r\n probability of denial of service: " + ((double) rejectedCount / (count + rejectedCount))
                + ";\r\n average time for server --- free: " + sumUnitsFreeTime / (curTime * servers.length)
                + ", busy: " + sumUnitsBusyTime / (curTime * servers.length)
                + ";\r\n average number of free servers: " + avFreeSevers
                + ";\r\n probability of number of free servers and relative downtime of each server"
        );
        for (int i = 0; i < servers.length; i++) {
            String sout = "{" + i + "}";
            while (sout.length() < 5) sout += " ";
            sout += servFreeSumTime[i] / curTime + "          ";
            while (sout.length() < 37) sout += " ";
            sout += "[" + i + "]";
            while (sout.length() < 42) sout += " ";
            sout += servers[i].sumFreeTime / curTime;
            System.out.println(sout);
        }
        String sout = "{" + servers.length + "}";
        while (sout.length() < 5) sout += " ";
        sout += servFreeSumTime[servers.length] / curTime;
        System.out.println(sout);

//        System.out.println("");
//        System.out.println("Number of changing of system conditions");
//        for (int i = changeConditionCount.length - 1; i >= 0; i--) {
//            System.out.println("{" + (i + 1) + "}->{" + i + "} : " + changeConditionCount[i][0]
//                    + "    " + "{" + (i + 1) + "}<-{" + i + "} : " + changeConditionCount[i][1]);
//
//        }

    }

    /**
     * Process event of job arrival.
     *
     * @param event
     */
    public void processCallEvent(QueueCallEvent event) {
        //Current time becomes equal to event time.
        curTime = event.getTime();
        //Next job arrival event is creating.
        makeNextCallEvent();
        //Search for free servers for job, if not then job will be added to queue
        //or will be aborted without service.
        blockBusy:
        {
            //Looking for free servver.
            for (ServiceUnit server : servers) {
                //If server is free, it will start to serve current job.
                if (server.currentJob == null) {
                    //Job goes to the server for service.
                    server.setJob(event.getJob(), curTime);
                    //Register time while system was in previous condition.
                    servFreeSumTime[freeServersNumber] += curTime - lastFreeServsNumberChangeTime;
                    //Save the time when condition is changed.
                    lastFreeServsNumberChangeTime = curTime;
                    //Number of free servers is decremented.
                    freeServersNumber--;
                    //Condition changing counter is incremented.
                    changeConditionCount[freeServersNumber][0]++;
                    //New event of service completion is created and added to event container.
                    QueueFreeEvent ev = new QueueFreeEvent(curTime + event.getJob().amount, server);
                    events.add(ev);
                    //Since a free server was found and the job was starting to serve,
                    //the search process is finished and this block is complete.
                    break blockBusy;
                }
            }
            //If free server was not found, then there is attempt to add the job to queue.
            boolean added = queue.add(event.getJob(), curTime);
            //If the job was rejected then deny counter is incremented.
            if (!added) rejectedCount++;
        }// end of blockBusy
    }

    /**
     * Process event of job service completion.
     *
     * @param event
     */
    public void processFreeEvent(QueueFreeEvent event) {
        //Counter of served jobs is incremented.
        count++;
        //Cuurent time becomes equal to event time.
        curTime = event.getTime();
        //Server is free.
        JobToServe outJob = event.getUnit().finJob(curTime);
        //Register job waiting time for service in queue.
        sumWaitingTime += outJob.getWaitingTime();
        //Учитывается время нахождения заявки в системе.
        //Register service time.
        sumServiceTime += outJob.amount;
        //If the queue is not empty, then service takes a job to serve from queue.
        if (queue.size() > 0) {
            //Job removed from queue.
            JobToServe job = queue.remove(0, curTime);
            //The server that became free, takes a job from queue to serve.
            event.getUnit().setJob(job, curTime);
            //Create new event that correspondent to time moment when service will be complete.
            QueueFreeEvent ev = new QueueFreeEvent(curTime + job.amount, event.getUnit());
            //Add new event to the event container.
            events.add(ev);
        } else {
            //If the quque is empty and server will be free, then queue system condition is changed
            //and a time interval while system was in previous condition is registered.
            servFreeSumTime[freeServersNumber] += curTime - lastFreeServsNumberChangeTime;
            lastFreeServsNumberChangeTime = curTime;
            changeConditionCount[freeServersNumber][1]++;
            freeServersNumber++;
        }
    }

    /**
     * Creates a next arrival event and adds it to the event container.
     */
    private void makeNextCallEvent() {
        JobToServe job = inc.nextCall();
        QueueCallEvent evIn = new QueueCallEvent(curTime + job.time, job);
        events.add(evIn);
    }

    public static long factorialUsingRecursion(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorialUsingRecursion(n - 1);
    }

    public static double probWithLimitsQueue(double lambda, double mu, int serviceNumber, int maximalSizeInQueue, int n){
        double rho= lambda/mu;
        double rateRhoC = rho/serviceNumber;
        double firstPart_p0 = 0;
        for (int i=0; i< serviceNumber; i++){
            firstPart_p0 += Math.pow(rho,i)/factorialUsingRecursion(i);
        }
        double p0 = 1/(firstPart_p0 + Math.pow(rho,serviceNumber)/factorialUsingRecursion(serviceNumber)*((1-Math.pow(rateRhoC, maximalSizeInQueue-serviceNumber+1))/(1-rateRhoC)));

        if (rateRhoC==1) {
            p0 = 1/(firstPart_p0 + Math.pow(rho, serviceNumber)/factorialUsingRecursion(serviceNumber)*(maximalSizeInQueue - serviceNumber+1));

        }

        if (0<=n && n < serviceNumber){
            return Math.pow(rho,n)/factorialUsingRecursion(n)*p0;
        }
        else if(serviceNumber<=n&&n<=maximalSizeInQueue){
            return Math.pow(rho,n)*p0/(factorialUsingRecursion(serviceNumber)*Math.pow(serviceNumber,n-serviceNumber));
        }
        else {
            return 0;
        }

    }


    /**
     * Entry point to the program.
     *
     * @param args Is not used.
     */
    public static void main(String[] args) {
        Model_3 that = new Model_3();
        that.go();
        double lambda = 3.75;
        int roomSize = 11;
        int serviceNumber = 4;
        double mu = 0.61;
        int maximalSizeInQueue = serviceNumber + roomSize;
//      calculate the effective arrival rate
        double lambdaEff = probWithLimitsQueue(lambda, mu, serviceNumber, maximalSizeInQueue, maximalSizeInQueue) * lambda;

        System.out.println("--------------The probability of i customer in system----------------");
        for (int j = 0; j <= maximalSizeInQueue; j++) {
            System.out.println("p{" + j + "}" + "\t:" + probWithLimitsQueue(lambdaEff, mu, serviceNumber, maximalSizeInQueue, j));
        }
        System.out.println("++++++++++++++++++++++++++++++");
        for (int i = 0; i <= serviceNumber; i++) {
            double prob = 0;
            if (i == 0) {
                for (int j = serviceNumber - i; j <= maximalSizeInQueue; j++) {
                    prob += probWithLimitsQueue(lambdaEff, mu, serviceNumber, maximalSizeInQueue, j);
                }
            } else {
                prob = probWithLimitsQueue(lambdaEff, mu, serviceNumber, maximalSizeInQueue, serviceNumber - i);
            }

            System.out.println("Probability of " + i + " free service \t p{" + i + "}" + "\t:" + prob);
        }
    }
}
