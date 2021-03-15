package queue;

import java.util.Locale;

/**
 * Class to create and output histograms.
 *
 * @author Alexander Mikhailovich Kovshov
 * 23.03.2017 12:09:40
 */
public class Histogram {
    private final int[] bins;
    private final double minValue;
    private final double maxValue;
    private final double binDiff;
    private int count = 0;

    /**
     * Creates new instance for single data set.
     * @param bins Number of histogram bins. All the bins have the same width.
     * @param minValue Right bound of the first bin.
     * @param maxValue Left bound of the last bin.
     */
    public Histogram(int bins, double minValue, double maxValue) {
        this.bins = new int[bins + 2];//Two additional bins for out-of-bounds values.
        this.minValue = minValue;
        this.maxValue = maxValue;
        binDiff = (maxValue - minValue) / bins;
    }

    /**
     * Constructor with precalculated array of bins.
     * @param bins Precalculated array of bins.
     * @param minValue Right bound of the first bin.
     * @param maxValue Left bound of the last bin.
     */
    private Histogram(int[] bins, double minValue, double maxValue) {
        this(bins.length - 2, minValue, maxValue);
        System.arraycopy(bins, 0, this.bins, 0, bins.length);
    }

    /**
     * Creates another histogram with integrated bins.
     * @return Integrated histogram.
     */
    public Histogram makeIntegratedHistogram() {
        int[] inBins = new int[bins.length];
        inBins[0] = bins[0];
        for (int i = 1; i < inBins.length; i++) {
            inBins[i] = inBins[i - 1] + bins[i];
        }
        return new IntegratedHistogram(inBins, minValue, maxValue);
    }

    /**
     * Puts a value into corresponding histogram bin.
     * @param value
     */
    public void put(double value) {
        int k = (int) ((value - minValue) / binDiff + 1);
        if (k < 0) k = 0;
        if (k >= bins.length) k = bins.length - 1;
        bins[k]++;
        count++;
    }

    /**
     * Returns array of bins.
     * @return array of bins.
     */
    public int[] getBins() {
        return bins;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US,    "            < % 7.4f   -> % 7.4f    %d\r\n", minValue, (double) bins[0] / count , bins[0]));
        for (int i = 1; i < bins.length - 1; i++) {
            if (!(this instanceof IntegratedHistogram))
                sb.append(String.format(Locale.US, " % 7.4f  =:= % 7.4f   -> % 7.4f    %d\r\n",
                        minValue + binDiff * (i - 1), minValue + binDiff * i, (double) bins[i] / count , bins[i]));
            else
                sb.append(String.format(Locale.US, "            < % 7.4f   ->   %d\r\n",
                        minValue + binDiff * i, bins[i]));
        }
        if (!(this instanceof IntegratedHistogram))
            sb.append(String.format(Locale.US,    "          >=  % 7.4f   -> % 7.4f    %d\r\n", maxValue, (double) bins[bins.length - 1] / count , bins[bins.length - 1]));
        else
            sb.append(String.format(Locale.US,    "            <  infinity   ->   %d\r\n", bins[bins.length - 1]));
        return sb.toString();
    }

    /**
     * Inner class for histogram with integrated bins.
     */
    private final static class IntegratedHistogram extends Histogram {

        private IntegratedHistogram(int[] bins, double minValue, double maxValue) {
            super(bins, minValue, maxValue);
        }

        @Override
        public void put(double value) {
            throw new UnsupportedOperationException("Put operation is not supported for IntegratedHistogram.");
        }

    }

}

