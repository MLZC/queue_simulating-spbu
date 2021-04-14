package random;

/**
 * Created on 17.04.2015 12:15:42
 * @author Alexander Mikhailovich Kovshov
 */
public class PrescribedSequence extends RandomSequence {

    private final double[] data;
    int k;

    public PrescribedSequence(double[] data) {
        this.data = data;
        k = 0;
    }


    @Override
    public double getNext() {
        return data[k++];
    }

}
