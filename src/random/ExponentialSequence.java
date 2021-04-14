package random;

import queue.*;

/**
 * Последовательность значений экспоненциально распределённой случайной величины.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 04.04.2013 22:41:22
 * Corrected 23.11.2018
 */
public class ExponentialSequence  extends RandomBaseSequence{

    private ExponentialSequence(double mean) {
        super(mean, mean * mean);
    }

    private ExponentialSequence(double mean, long seed) {
        super(mean, mean * mean, seed);
    }

    /**
     * Creates a new instance by mean.
     * @param mean
     * @return new instance.
     */
    public static ExponentialSequence createByMean(double mean){
        return new ExponentialSequence(mean);
    }

    /**
     * Creates a new instanse by mean and the seed of the random sequence.
     *
     * @param mean
     * @param seed
     * @return
     */
    public static ExponentialSequence createByMeanWithSeed(double mean, long seed){
        return new ExponentialSequence(mean, seed);
    }

    /**
     * Returns next value of the random sequence.
     * @return
     */
    @Override
    public double getNext() {
        return  -Math.log(1 - getRand().nextDouble()) * getMean();
    }

    /**
     * Test of this class.
     * @param args Does not used.
     */
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        int attempts = 10000000;
        Histogram histo = new Histogram(10, 0, 0.1);
        ExponentialSequence expSeq = new ExponentialSequence(.5);
        double sum = 0;
        double sum2 = 0;
        for (int i = 0; i < attempts; i++) {
            double value = expSeq.getNext();
            sum += value;
            sum2 += value * value;
            histo.put(value);
        }
        System.out.println(histo);
        double avg = sum / attempts;
        double var = avg * avg * attempts - 2 * avg * sum + sum2;
        var /= attempts;
        System.out.println("avg: " + avg + " var: " + var);
    }

}
