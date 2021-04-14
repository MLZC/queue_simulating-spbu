package random;

/**
 * Generator of the uniformly distributed random values in [0,1).
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 04.04.2013 17:47:08
 * Corrected 23.11.2018
 */
public class UniformSequence extends RandomBaseSequence{

    /** Random values multiplier. */
    private double multiplier = 1.;
    /** Shift of the random values. */
    private double increment = 0.;


    private UniformSequence(double mean) {
        super(mean, mean * mean / 12.);
        multiplier = mean * 2.;
    }

    private UniformSequence(double mean, double deviation) {
        super(mean, deviation * deviation / 3.);
        multiplier = deviation * 2.;
        increment = mean - deviation;
    }


    private UniformSequence(double mean, long seed) {
        super(mean, mean * mean / 12., seed);
        multiplier = mean * 2.;
    }

    private UniformSequence(double mean, double deviation, long seed) {
        super(mean, deviation * deviation / 3., seed);
        multiplier = deviation * 2.;
        increment = mean - deviation;
    }

    /**
     * Creates a new instance by mean. The length of the domain equals two means.
     * @param mean
     * @return
     */
    public static UniformSequence createByMean(double mean){
        return new UniformSequence(mean);
    }

    /**
     * Creates a new instance by mean and the half of the length of the domain.
     * @param mean
     * @param deviation half of the length of the domain.
     * @return
     */
    public static UniformSequence createByMeanAndDeviation(double mean, double deviation){
        return new UniformSequence(mean, deviation);
    }

    /**
     * Creates a new instance by mean and variance.
     * @param mean
     * @param variance
     * @return
     */
    public static UniformSequence createByMeanAndVariance(double mean, double variance){
        return new UniformSequence(mean, Math.sqrt(3 * variance));
    }

    /**
     * Creates a new instance by bounds of domain.
     * @param min left domain bound.
     * @param max right domain bound.
     * @return
     */
    public static UniformSequence createByInterval(double min, double max){
        return new UniformSequence((max + min) / 2., (max - min) / 2.);
    }


    /**
     * Creates a new instance by mean and the seed, generating the random sequence.
     * The length of the domain equals two means.
     * @param mean
     * @return
     */
    public static UniformSequence createByMeanWithSeed(double mean, long seed){
        return new UniformSequence(mean, seed);
    }


    /**
     * Returns the next value of the random sequence.
     * @return
     */
    @Override
    public double getNext() {
        return getRand().nextDouble() * multiplier + increment;
    }

}
