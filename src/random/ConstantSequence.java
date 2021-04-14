package random;

/**
 * The sequence of constant values.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 04.04.2013 21:25:40
 * Corrected 30.11.2018
 */
public class ConstantSequence extends RandomBaseSequence {

    /**
     * @param mean value of sequence elements.
     * @return new instance.
     */
    public static RandomSequence createByMean(double mean) {
        return new ConstantSequence(mean);
    }

    private ConstantSequence(double mean) {
        super(mean, 0);
    }

    /**
     * Return constant value, that equals to mean.
     * @return mean.
     */
    @Override
    public double getNext() {
        return getMean();
    }

}
