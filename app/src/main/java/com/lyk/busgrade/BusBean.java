package com.lyk.busgrade;

import java.util.List;

/**
 * Created by Nian on 17/6/18.
 */

public class BusBean {

    private LineResults0 lineResults0;

    public LineResults0 getlineResults0() {
        return lineResults0;
    }

    public void setlineResults0(LineResults0 lineResults0) {
        this.lineResults0 = lineResults0;
    }
    @Override
    public String toString() {
        return "LineResults0 [showapi_res_body=" + lineResults0 + "]";
    }

    class LineResults0 {
        private String direction;
        private List<LineResults0Stops> stops;

        public List<LineResults0Stops> getStops() {
            return stops;
        }
        public void setStops(List<LineResults0Stops> stops) {
            this.stops = stops;
        }

        public String getDirection() {
            return direction;
        }
        public void setDirection(String direction) {
            this.direction = direction;
        }
        @Override
        public String toString() {
            return "HomeNewsBeanBody [Stops=" + stops + ", direction="
                    + direction + "]";
        }


        class LineResults0Stops {
            private String zdmc;
            private String id;

            public void setZdmc(String zdmc) {
                this.zdmc = zdmc;
            }
            public String getZdmc() {
                return zdmc;
            }
            public void setDesc(String id) {
                this.id = id;
            }
            public String getDesc() {
                return id;
            }
        }

    }




    private LineResults0 lineResults1;

    public LineResults0 getlineResults1() {
        return lineResults1;
    }

    public void setlineResults1(LineResults0 lineResults1) {
        this.lineResults1 = lineResults1;
    }

}
