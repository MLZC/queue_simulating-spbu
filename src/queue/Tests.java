package queue;

import java.util.*;

/**
 * Created on 24 Feb. 2021 10:16:04
 * @author Alexander Mikhailovich Kovshov
 */
public class Tests {

    /**
     *
     * Makes sorted array of random numbers.
     *
     * @param dim length of array
     * @return sorted in ascending order array of random double numbers between
     * 0 and 1 with length specified.
     */
    public static double[] randomArray(int dim) {
        double[] ret = new double[dim];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Math.random();
        }
        Arrays.sort(ret);
        return ret;
    }


    /**
     * Test of method randomArray.
     */
    public static void test_randomArray() {
        double[] array = randomArray(10);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
            System.out.println(""
                    + String.format(Locale.US, "% 3d", i) + " :   "
//                    + String.format(Locale.US, "% 5.3f", new Double(d)));
                    + String.format(Locale.US, "% 5.3f", d));
        }
    }


    /**
     * Makes an array of real numbers that are the differences of neighbor numbers
     * in another array, obtained by method randomArray.
     *
     * @param dim dimension of output array
     * @return
     */
    public static double[] randomArrayDiff(int dim) {
        double[] arr = randomArray(dim + 1);
        double[] ret = new double[dim];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[i + 1] - arr[i];
        }
        Arrays.sort(ret);
        return ret;
    }



    /**
     * Test of method randomArrayDiff.
     */
    public static void test_randomArrayDiff() {
        double[] array = randomArrayDiff(10);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
            System.out.println(""
                    + String.format(Locale.US, "% 3d", i) + " :   "
                    + String.format(Locale.US, "% 8.3f", d));
        }
    }



    /**
     * Makes a histogram with given number of bins.
     * Returned array has length larger than given number of bins on 2,
     * that permit to count values that less than minimal specified value and grater than maximal specified value.
     * @param bins number of histogram bins (columns)
     * @param minValue
     * @param maxValue
     * @param data array of data to analyse
     * @return
     */
    public static int[] makeHistogram(int bins, double minValue, double maxValue, double[] data){
        int[] histo = new int[bins + 2];
        double valDiff = maxValue - minValue;
        for (int i = 0; i < data.length; i++) {
            int k = (int) ((data[i] - minValue) / valDiff * bins + 1);
            if(k < 0) k = 0;
            if(k >= histo.length) k = histo.length - 1;
            histo[k]++;
        }
        return histo;
    }



    /**
     * Output histogram to colsole.
     */
    public static void printHisto(double minValue, double maxValue, int[] histo){
        double binDiff = (maxValue - minValue) / (histo.length - 2);
        System.out.println("          < " + "   ->   " + histo[0]);
        for (int i = 1; i < histo.length - 1; i++) {
            System.out.println("    " + String.format(Locale.US, "% 8.6f", new Double(minValue + binDiff * (i - 1))) + "   ->   " + histo[i]);
        }
        System.out.println(" >= " + String.format(Locale.US, "% 8.6f", new Double(maxValue)) + "   ->   " + histo[histo.length - 1]);

    }



    /**
     * Test histogram.
     */
    public static void testHisto(){
        double[] data = { 0.1, 0.3, 0.5, 0.2, 0.8, 1.3, 2.1, 3.4, 5.5};
        int[] histo = makeHistogram(2, 1, 3, data);
        printHisto(1, 3, histo);
    }



    /**
     * Makes histogram of numbers from array, that is obtained by method randomArrayDiff.
     */
    public static void test_randomArrayDiffHisto() {
        double[] array = randomArrayDiff(100000);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
//            System.out.println(""
//                    + String.format(Locale.US, "% 3d", i) + " :   "
//                    + String.format(Locale.US, "% 8.3f", d));
        }

        double inf = 0;
        double sup = 0.00005;
        int[] histo = makeHistogram(10, inf, sup, array);
        printHisto(inf, sup, histo);
        double p = 1 - Math.exp(-100000*0.000005);
        System.out.println("p: " + p);
    }


    public static double randExp(double lambda) {
        // t = -ln(1 - y) * 1/λ
        return -Math.log(1. - Math.random()) / lambda;
    }

    public static void test_randomExpHisto() {
        int n = 10000;
        double[] array = new double[n];
        for (int i = 0; i < array.length; i++) {
            array[i] = randExp(n);
//            System.out.println(""
//                    + String.format(Locale.US, "% 3d", i) + " :   "
//                    + String.format(Locale.US, "% 8.3f", d));
        }

        double inf = 0;
        double sup = 0.0005;
        int[] histo = makeHistogram(10, inf, sup, array);
        printHisto(inf, sup, histo);
    }



    public static double sumUni() {
        return Math.random() + Math.random();
    }

    public static void test_sumOfTwoUni() {
        int n = 10000;
        double[] array = new double[n];
        for (int i = 0; i < array.length; i++) {
            array[i] = sumUni();
        }

        double inf = 0;
        double sup = 2;
        int[] histo = makeHistogram(10, inf, sup, array);
        printHisto(inf, sup, histo);
    }

    public static double prodOfTwoUni() {
        return Math.random() * Math.random();
    }

    public static void test_prodOfTwoUni() {
        int n = 10000;
        double[] array = new double[n];
        for (int i = 0; i < array.length; i++) {
            array[i] = prodOfTwoUni();
        }

        double inf = 0;
        double sup = 1;
        int[] histo = makeHistogram(10, inf, sup, array);
        printHisto(inf, sup, histo);
    }


    /**
     * Invokes the method eventsProbabilityPerTimeInterval several times to calculate
     * probability of various number of events on specified time interval with
     * specified arraival rate.
     *
     */
    private static void testEventsProbabilityPerTimeInterval(){
        //Full number of events in experiment.
        int n_k = 1000000;
        //Average interval between events.
        double k = 2.5;
        //Time interval duration, on which events are calculated.
        int timeIncr = 15;
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 0);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 1);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 2);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 3);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 4);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 5);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 6);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 7);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 8);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 9);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 10);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 11);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 12);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 13);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, 14);

    }



    /**
     * Calculates the statistical probability of specified number of events on
     * specified time interval with known arraival rate.
     *
     * @param n_k number of events
     * @param k average interval between the events
     * @param timeIncr time interval duration, on which events are calculated
     * @param evNum number of events to calculate its probability
     */
    private static void eventsProbabilityPerTimeInterval(int n_k, double k, int timeIncr, int evNum) {
        //Output parameters
        double eventsProbability = 0;
        //An array of random values from 0 to 1, ordered in ascending order.
        double[] rnf = randomArray(n_k);
        //Multiply the obtained random values by the duration of the experiment.
        double fullTime = n_k * k;
        for (int i = 0; i < rnf.length; i++) {
            rnf[i] *= fullTime;
        }

        //Current time.
        int curTime = timeIncr;
        //Counter of time intervals.
        int countIncr = 0;
        //Loop on time intervals while current time is less than the duration of the experiment.
        loop1:
        for (int cnt = 0; ; curTime += timeIncr, countIncr++) {
            //Counting events on the time interval.
            int events = 0;
            while (rnf[cnt] <= curTime){
                //If the event time is less than the end of the current time interval, then the counter is incremented.
                events++;
                //The next event is considered, but if the experiment time is over, exit the loop.
                if(++cnt >= rnf.length) break loop1;
            }
            //If the number of events in the current time interval is equal to the specified number
            //the counter of such events is increases by 1.
            if(events == evNum) {
                eventsProbability++;
            }

        }
        //Output
        System.out.println("number of the events: " + evNum + "\r\n" +
                "time interval duration: " + timeIncr + "\r\n" +
                "probability of number of events on time interval : " + (eventsProbability / countIncr) + "\r\n" +
                "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        poissonProcess(1/k,evNum,timeIncr);
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
    }

    public static long factorialUsingRecursion(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorialUsingRecursion(n - 1);
    }

    public static void poissonProcess(double lambda,int k, int t) {
        double prob = Math.pow(lambda*t,k)*Math.exp(-lambda*t)/factorialUsingRecursion(k);
        System.out.println("lambda:"+lambda+"\t"+"k:"+k+"\t"+"t:"+t+"\t"+"probability:"+prob);
    }


    public static void main(String[] args) {
//        test_randomArray();
//        test_randomArrayDiff();
//        testHisto();
//        test_randomArrayDiffHisto();
//        test_randomExpHisto();
//        test_prodOfTwoUni();
//        testEventsProbabilityPerTimeInterval();
          test_sumOfTwoUni();
    }

}
