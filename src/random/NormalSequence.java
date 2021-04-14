package random;

import queue.*;

/**
 * Generator of the normally distributed random values.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 03.04.2015 15:34:26
 */
public class NormalSequence extends RandomBaseSequence {

    /** The sequence of the exponentially distributed random values. */
    private final ExponentialSequence expSeq;

    private boolean secondValue = false;
    private double value = 0;

    /**
     * Constructor by mean and variance.
     *
     * @param mean
     * @param variance
     */
    private NormalSequence(double mean, double variance) {
        super(mean, variance);
        expSeq = ExponentialSequence.createByMean(2 * variance);
    }

    /**
     * Creates a new instance by mean and variance..
     * @param mean
     * @param variance
     * @return new instance.
     */
    public static NormalSequence createByMeanAndVariance(double mean, double variance){
        return new NormalSequence(mean, variance);
    }

    /**
     * Returns the next random value by Box-Muller method
     * @return
     */
    public double getNext1() {
        if (secondValue) {
            secondValue = false;
            return value;
        }
        secondValue = true;
        double phi = getRand().nextDouble() * 2 * Math.PI;
        double r = Math.sqrt(expSeq.getNext());
        double cos = Math.cos(phi);
        double sin = Math.sqrt(1. - cos * cos);
        if (phi > Math.PI) sin = -sin;
        value = r * sin + getMean();
        return  r * cos + getMean();
    }

    /**
     * Returns the next random value by the polar modification of
     * the Box-Muller method
     * @return
     */
    @Override
    public double getNext() {
        if (secondValue) {
            secondValue = false;
            return value;
        }
        secondValue = true;
        double u;
        double v;
        double s;
        do {
            u = getRand().nextDouble() * 2 - 1;
            v = getRand().nextDouble() * 2 - 1;
            s = u * u + v * v;
        } while (s > 1 || s == 0);
        double r = -Math.log(s) * 2 / s * getVariance();
        r = Math.sqrt(r);
        value = r * u + getMean();
        return  r * v + getMean();
    }

    /**
     * Проверка работы класса.
     * @param args Не используется.
     */
    public static void main(String[] args) {
        test();

    }

    private static void test() {
        int attempts = 10000000;
        NormalSequence normSeq = new NormalSequence(0, 1);
        Histogram histo = new Histogram(6, -3, +3);
        double sum = 0;
        double sum2 = 0;
        for (int i = 0; i < attempts; i++) {
            double value = normSeq.getNext1();
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