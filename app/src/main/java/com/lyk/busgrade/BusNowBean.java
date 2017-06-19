package com.lyk.busgrade;

import java.util.List;

/**
 * Created by Nian on 17/6/19.
 */

public class BusNowBean {
    private List<Cars> cars;

    public List<Cars> getCars() {
        return cars;
    }

    public void setCars(List<Cars> imageurls) {
        this.cars = cars;
    }

    public class Cars {
        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public String getStopdis() {
            return stopdis;
        }

        public void setStopdis(String stopdis) {
            this.stopdis = stopdis;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String terminal;
        public String stopdis;
        public String distance;
        public String time;


    }
}
