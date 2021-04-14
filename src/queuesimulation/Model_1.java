package queuesimulation;


import java.util.Arrays;

/**
 * Simulating of queuing system without waiting room
 * with Poisson arraival stream.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created   29.03.2013  2:17:39
 * Modified  26.03.2018 16:38:10
 * Corrected 03.11.2018 14:09:37
 */
public class Model_1 {


    /**
     * Returns a random value of exponentially distributed stochastic variable.
     *
     * @param lambda Rate of exponential distribution (arrival rate).
     * @return Random value.
     */
    public static double randExp(double lambda) {
        return -Math.log(1 - Math.random()) / lambda;
    }

    /**
     * Calculates the probability for a job to be not served,
     * that is the probability that all servers are busy
     * at the arrival time moment.
     *
     * @param attempts             total number of jobs
     * @param numOfUnits           number of servers
     * @param avg_interval         average interval between arrivals
     * @param serviceTime          average service time
     * @param serviceTimeDifferent maximal service time deviation
     * @return
     */
    private static double taskNunits(int attempts, int numOfUnits,
                                     double avg_interval, double serviceTime, double serviceTimeDifferent) {

        //Returned value --- the denial of service probability.
        double ret = 0.;
        //Average number of job arrivals per time unit.
        double lambda = 1. / avg_interval;
        //Count of jobs that was not serviced.
        int busyCount = 0;
        //The rest of service time for every server, that is how long it will be busy.
        double[] units = new double[numOfUnits];
        //Initially all servers are free.
        Arrays.fill(units, 0.);
        //First job occupies the first server for average service time.
        units[0] = serviceTime;
        //Loop over all jobs.
        for (int i = 0; i < attempts; i++) {
            //Time interval between job arrival.
            double interval = randExp(lambda);
            //Loop over all servers. If server is busy then the rest of service time
            //will be decreased by passed time interval.
            for (int j = 0; j < units.length; j++) {
                if (units[j] > 0) {
                    units[j] -= interval;
                }
            }
            //Looking for free servers block.
            blockBusy:
            {
                //Loop over all servers.
                for (int j = 0; j < units.length; j++) {
                    //If the rest of the service time is not positive,
                    //the server is free.
                    if (units[j] <= 0) {
                        //Service time is assigned to server.
                        //Service time equals average value plus random deviation.
                        //That is service time is uniformly distributed on interval
                        //[serviceTime - serviceTimeDifferent, serviceTime + serviceTimeDifferent]
                        units[j] = serviceTime + serviceTimeDifferent * 2. * (0.5 - Math.random());
                        //If a free server was found then go out of the block.
                        break blockBusy;
                    }
                }
                //If no servers were found inside the loop, so there was not break from the block,
                //then the count of denials is incremented.
                busyCount++;
            }
        }
        //The relation of the denials to total number of jobs is the denial probability.
        ret = (double) busyCount / (double) attempts;
        return ret;
    }

    /**
     * Invokes the queuing system simulation.
     */
    private static void test_TaskNUnits() {
        int n = 10000000;
        double dt = 1;
        int nu = 4;
        double srv = 1;
        double srv_diff = 0.3;
        System.out.println("------------------------------------"
                + "\r\n number of jobs: " + n
                + "\r\n number of servers: " + nu
                + "\r\n average time between arrivals: " + dt
                + "\r\n average service time: " + srv
                + "\r\n maximal service time deviation: " + srv_diff
                + "\r\n denial of service probability: " + taskNunits(n, nu, dt, srv, srv_diff));
        double rho = srv / dt;
        double temp_sum = 0;
        for (int i = 0; i <= nu; i++) {
            temp_sum += Math.pow(rho, i) / factorialUsingRecursion(i);
        }
        double prob = Math.pow(rho, nu) / (factorialUsingRecursion(nu) * temp_sum);
        System.out.println("------------------------------------"
                + "\r\n number of servers: " + nu
                + "\r\n average time between arrivals: " + dt
                + "\r\n average service time: " + srv
                + "\r\n theoretical probability:" + prob
        );
    }

    public static long factorialUsingRecursion(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorialUsingRecursion(n - 1);
    }

    public static void main(String[] args) {
        test_TaskNUnits();
    }


}
