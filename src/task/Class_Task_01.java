package task;

import random.*;
import queue.Histogram;

/**
 * U[2,4]^2 * E(1)
 * Pr[3,4] ?
 * E(2) + N(1,3)^2
 * Pr[0,2] ?
 * E^2(2) + U[-1,1]
 * Pr[0,2]?
 */

public class Class_Task_01 extends RandomSequence {
    //    UniformSequence u = UniformSequence.createByInterval(1, 3);
//    SquaredSequence s = new SquaredSequence(u);
//    ExponentialSequence e = ExponentialSequence.createByMean(1);
    ExponentialSequence e = ExponentialSequence.createByMean(2);
    SquaredSequence e2 = new SquaredSequence(e);
    UniformSequence u = UniformSequence.createByInterval(0, 2);


//    public Class_Task_01() {
//    }

    @Override
    public double getNext() {
        return e2.getNext() + u.getNext();
    }

    public static void main(String[] args) {
        Class_Task_01 r = new Class_Task_01();
        Histogram hist = new Histogram(1, -1, 1);
        for (int i = 0; i < 10000000; i++) {
            hist.put(r.getNext());
        }
        System.out.println(hist);
    }

}
