package object;

import java.util.PriorityQueue;
import queuesimulation.Model_3;

/**
 * General description of any event in queueing system.
 *
 * @author Alexander Mikhailovich Kovshov
 * Created 02.04.2018 19:42:27
 * Corrected 29.11.2018
 */
public abstract class QueueEvent implements Comparable<QueueEvent> {

    /** Event time. */
    private double time;

    /** Constructor. */
    public QueueEvent(double time) {
        this.time = time;
    }

    /**
     * Gets the time of the event.
     * @return Time of this event.
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the new time of the event.
     * @param time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Method from Comparable interface;
     * introduces the order-relation in the set of events with respect to time.
     */
    @Override
    public int compareTo(QueueEvent o) {
        return time < o.time ? -1 : time > o.time ? 1 : 0;
    }

    /**
     * Process the event.
     * @param model The main object.
     */
    public abstract void processEvent(Model_3 model);

    /**
     * Test PriorityQueue ordering.
     * @param args Not used.
     */
    public static void main(String[] args) {
        PriorityQueue<QueueEvent> evq = new PriorityQueue<>();
        QueueEvent[] eva = new QueueCallEvent[10];
        for (int i = 0; i < eva.length; i++) {
            eva[i] = new QueueCallEvent(Math.random(), null);
            evq.add(eva[i]);
            System.out.println(eva[i].time);
        }
        System.out.println("");
        for (QueueEvent ev : evq) {
            System.out.println(ev.time);
        }
        System.out.println("");
        while (evq.size() > 0) {
            System.out.println(evq.poll().time);
        }

    }

}
