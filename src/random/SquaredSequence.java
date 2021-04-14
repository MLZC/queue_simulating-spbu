package random;

/**
 * Generator of the squared random values of a stochastic variable.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 20.04.2017 12:09:03
 * Corrected 24.11.2018
 */
public class SquaredSequence extends RandomSequence {

    private final RandomSequence rs;

    /**
     * Constructor.
     * @param rs Any stochastic variable.
     */
    public SquaredSequence(RandomSequence rs) {
        this.rs = rs;
    }

    /**
     * Returns next value of the squared random sequence.
     * @return
     */
    @Override
    public double getNext() {
        double value = rs.getNext();
        return value * value;
    }

}
