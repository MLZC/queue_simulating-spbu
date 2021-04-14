package random;


import java.util.Random;

/**
 * General description of random values generator with
 * internal generator of the uniform distributed random values.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 29.05.2017 14:39:30
 * Corrected 23.11.2018
 */
public abstract class RandomBaseSequence extends RandomSequence {
    private final Random rand;
    private double mean = 0;
    private double variance = 1;

    public RandomBaseSequence() {
        rand = new Random();
        mean = 0;
    }

    public RandomBaseSequence(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
        rand = new Random();
    }

    public RandomBaseSequence(long seed) {
        rand = new Random(seed);
    }

    public RandomBaseSequence(double mean, double variance, long seed) {
        rand = new Random(seed);
        this.mean = mean;
        this.variance = variance;
    }

    protected Random getRand() {
        return rand;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

}
