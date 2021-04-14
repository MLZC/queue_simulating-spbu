package queuesimulation;

import java.util.PriorityQueue;

/**
 * At a large hotel, taxi cabs arrive at a rate of 15 per hour, and parties of riders
 * arrive at the rate of 12 per hour. Whenever taxicabs are waiting, riders are served
 * immediately upon arrival. Whenever riders are waiting, taxicabs are loaded immediately
 * upon arrival. A maximum of three cabs can wait at a time (other cabs must go elsewhere).
 * a) Let p(i,j) be the steady-state probability of there being i parties of riders and j
 * taxicabs waiting at the hotel. Write the state transition equation for the system.
 * b) Calculate the expected number of cabs waiting and the expected number of parties waiting.
 * c) Calculate the expected waiting time for cabs and the expected waiting for parties.
 * (For cabs, compute the average among those that do not go elsewhere.)
 * d) In words, what would be the impact of allowing four cabs to wait at a time?
 * Hall 5.22
 *
 * @author Alexander Mikhailovich Kovshov
 * 17.03.2018 16:39:16
 */
public class TaxiHotelModel {

    PriorityQueue<QueueEvent> eventsSequence;   //Sequence of cabs and riders arrivals.
    double[] conditonTime = new double[104];     //Total time of each condition.
    int taxiWaitingRoom = 3;                    //How many cabs can wait for riders.
    int rideWaitingRoom = 100;                  //How many riders can wait for cab.
    int condition = taxiWaitingRoom;            //Number of current condition.
    double finitTime = 1000000;                 //Final time of simulation.
    double lambda = 15;                         //Cabs' arrival rate.
    double mu = 12;                             //riders' arrival rate.
    double lastEventTime = 0;                   //The time moment when system condition was changed.
    int taxiCount = 0;                          //Count of cabs with riders.
    int riderCount = 0;                         //Count of riders.
    double taxiWaitTime = 0;                    //Total waiting time for cabs.
    double riderWaitTime = 0;                   //Total waiting time for riders.

    public TaxiHotelModel() {
        this.eventsSequence = new PriorityQueue<QueueEvent>();
        QueueEvent ev = new QueueEvent();
        ev.time = randExp(lambda);
        ev.type = QueueEvent.TAXI;
        eventsSequence.add(ev);
        ev = new QueueEvent();
        ev.time = randExp(mu);
        ev.type = QueueEvent.RIDER;
        eventsSequence.add(ev);
        lastEventTime = 0;

    }

    /**
     * Returns.
     *
     * @param a rate parameter of the exponential distrubution
     * @return a value of the exponential distributed random variable
     */
    private double randExp(double a) {
        if (a <= 0) {
            throw new RuntimeException("Non positive rate 'a' " + a);
        }
        return -Math.log(1 - Math.random()) / a;
    }

    public static double prob_general_poisson(double rho, double i, double j) {
        double q = 3 - j + i;
        double p0 = 1 - rho;
        return Math.pow(rho, q) * p0;
    }

    public static void general_poisson(double lambda, double mu, int N) {
        double rho = lambda / mu;
        double Lt = 0;
        System.out.println("---------------------------------------");
        for (int j = 1; j < 4; j++) {
            double p = prob_general_poisson(rho, 0, j);
            System.out.println(0 + "\t" + "\t" + j + "\t" + p);
            Lt += (j * p);
        }
        double Lp = 0;
        for (int i = 0; i <= N; i++) {
            double p = prob_general_poisson(rho, i, 0);
            System.out.println(i + "\t" + "\t" + 0 + "\t" + p);
            Lp += (i * p);
        }
        System.out.println("---------------------------------------");
        System.out.println("taxi wait " + (Lt / lambda) + "      taxi av. queue " + Lt);
        System.out.println("rider wait " + (Lp / lambda) + "      rider av. queue " + Lp);

    }

    public static void main(String[] args) {
        TaxiHotelModel model = new TaxiHotelModel();
        while (model.lastEventTime < model.finitTime) {
            QueueEvent ev = model.eventsSequence.poll();
            ev.processEvent();
        }
        for (int i = 0; i < model.conditonTime.length; i++) {
            System.out.println("" + (i - model.taxiWaitingRoom) + "     "
                    + (model.conditonTime[i] / model.lastEventTime));

        }
        System.out.println("taxi in job   " + model.taxiCount + "      taxi wait " +
                (model.taxiWaitTime / model.taxiCount) + "      taxi av. queue "
                + (model.taxiWaitTime / (double) model.lastEventTime));
        System.out.println("riders in job " + model.riderCount + "     rider wait " +
                (model.riderWaitTime / model.riderCount) + "     rider av. queue " +
                (model.riderWaitTime / (double) model.lastEventTime));
        //  denote lambda as the rider's arrival rate and mu as the cars' arrival rate
        general_poisson(model.mu, model.lambda, model.conditonTime.length);
    }

    private class QueueEvent implements Comparable<QueueEvent> {

        public static final int TAXI = 0;
        public static final int RIDER = 1;

        double time;
        int type;

        void processEvent() {
            QueueEvent newEvent = new QueueEvent();
            newEvent.type = type;
            newEvent.time = time + randExp(type == TAXI ? lambda : type == RIDER ? mu : 1);
            eventsSequence.add(newEvent);

            int queueLength = condition - taxiWaitingRoom;
            double timeInterval = time - lastEventTime;
            if (queueLength < 0) {
                taxiWaitTime -= queueLength * timeInterval;

            } else {
                riderWaitTime += queueLength * timeInterval;
            }

            if (type == TAXI) {
                if (condition > 0) {
                    condition--;
                    taxiCount++;
                }
            } else if (type == RIDER) {
                if (condition < taxiWaitingRoom + rideWaitingRoom) {
                    condition++;
                    riderCount++;
                }
            }
            if (condition < conditonTime.length) {
                conditonTime[condition] += timeInterval;//time - lastEventTime;
            }
            lastEventTime = time;
        }

        ;

        @Override
        public int compareTo(QueueEvent o) {
            return time < o.time ? -1 : time > o.time ? 1 : 0;
        }

    }


}
/*
Condition -3; P 0.2000000000166873
Condition -2; P 0.16000000001334985
Condition -1; P 0.1280000000106799
Condition 0; P 0.10240000000854392
Condition 1; P 0.08192000000683515
Condition 2; P 0.06553600000546812
Condition 3; P 0.0524288000043745
Condition 4; P 0.041943040003499604
Condition 5; P 0.033554432002799686
Condition 6; P 0.026843545602239744
Condition 7; P 0.021474836481791798
Condition 8; P 0.01717986918543344
Condition 9; P 0.013743895348346752
Condition 10; P 0.010995116278677402
Condition 11; P 0.008796093022941922
Condition 12; P 0.007036874418353539
Condition 13; P 0.0056294995346828315
Condition 14; P 0.004503599627746265
Condition 15; P 0.0036028797021970126
Condition 16; P 0.00288230376175761
Condition 17; P 0.002305843009406088
Condition 18; P 0.0018446744075248708
Condition 19; P 0.0014757395260198965
Condition 20; P 0.0011805916208159173
Condition 21; P 9.44473296652734E-4
Condition 22; P 7.555786373221871E-4
Condition 23; P 6.044629098577497E-4
Condition 24; P 4.835703278861998E-4
Condition 25; P 3.8685626230895986E-4
Condition 26; P 3.094850098471679E-4
Condition 27; P 2.4758800787773436E-4
Condition 28; P 1.9807040630218748E-4
Condition 29; P 1.5845632504175E-4
Condition 30; P 1.267650600334E-4
Condition 31; P 1.0141204802672001E-4
Condition 32; P 8.112963842137602E-5
Condition 33; P 6.490371073710082E-5
Condition 34; P 5.192296858968065E-5
Condition 35; P 4.153837487174453E-5
Condition 36; P 3.323069989739562E-5
Condition 37; P 2.65845599179165E-5
Condition 38; P 2.12676479343332E-5
Condition 39; P 1.7014118347466562E-5
Condition 40; P 1.3611294677973248E-5
Condition 41; P 1.0889035742378599E-5
Condition 42; P 8.711228593902879E-6
Condition 43; P 6.968982875122304E-6
Condition 44; P 5.575186300097844E-6
Condition 45; P 4.460149040078275E-6
Condition 46; P 3.56811923206262E-6
Condition 47; P 2.854495385650096E-6
Condition 48; P 2.283596308520077E-6
Condition 49; P 1.8268770468160618E-6
Condition 50; P 1.4615016374528494E-6
Condition 51; P 1.1692013099622798E-6
Condition 52; P 9.353610479698238E-7
Condition 53; P 7.482888383758592E-7
Condition 54; P 5.986310707006874E-7
Condition 55; P 4.7890485656055E-7
Condition 56; P 3.8312388524844E-7
Condition 57; P 3.0649910819875204E-7
Condition 58; P 2.4519928655900167E-7
Condition 59; P 1.9615942924720132E-7
Condition 60; P 1.5692754339776108E-7
Condition 61; P 1.2554203471820886E-7
Condition 62; P 1.004336277745671E-7
Condition 63; P 8.034690221965368E-8
Condition 64; P 6.427752177572296E-8
Condition 65; P 5.142201742057837E-8
Condition 66; P 4.11376139364627E-8
Condition 67; P 3.2910091149170166E-8
Condition 68; P 2.632807291933613E-8
Condition 69; P 2.1062458335468905E-8
Condition 70; P 1.6849966668375125E-8
Condition 71; P 1.34799733347001E-8
Condition 72; P 1.078397866776008E-8
Condition 73; P 8.627182934208065E-9
Condition 74; P 6.901746347366452E-9
Condition 75; P 5.521397077893162E-9
Condition 76; P 4.41711766231453E-9
Condition 77; P 3.5336941298516237E-9
Condition 78; P 2.826955303881299E-9
Condition 79; P 2.2615642431050396E-9
Condition 80; P 1.8092513944840319E-9
Condition 81; P 1.4474011155872255E-9
Condition 82; P 1.1579208924697805E-9
Condition 83; P 9.263367139758244E-10
Condition 84; P 7.410693711806596E-10
Condition 85; P 5.928554969445277E-10
Condition 86; P 4.742843975556222E-10
Condition 87; P 3.794275180444978E-10
Condition 88; P 3.035420144355983E-10
Condition 89; P 2.428336115484787E-10
Condition 90; P 1.9426688923878292E-10
Condition 91; P 1.5541351139102634E-10
Condition 92; P 1.243308091128211E-10
Condition 93; P 9.946464729025689E-11
Condition 94; P 7.957171783220551E-11
Condition 95; P 6.36573742657644E-11
Condition 96; P 5.0925899412611535E-11
Condition 97; P 4.074071953008923E-11
Condition 98; P 3.259257562407139E-11
Condition 99; P 2.607406049925711E-11
Condition 100; P 2.085924839940569E-11
*/