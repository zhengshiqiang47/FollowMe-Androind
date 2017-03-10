package com.example.coderqiang.followme.Model;

import java.util.List;

/**
 * Created by CoderQiang on 2017/3/7.
 */

public class HistoryTrackData {


    /**
     * status : 0
     * message : 成功
     * total : 40
     * size : 10
     * distance : 848.13522221863
     * toll_distance : 0
     * start_point : {"longitude":119.19884391113,"latitude":26.056454427666,"coord_type":3,"loc_time":1488888010}
     * end_point : {"longitude":119.1988231067,"latitude":26.056335430387,"coord_type":3,"loc_time":1488893067}
     * points : [{"loc_time":1488893067,"location":[119.1988231067,26.056335430387],"create_time":"2017-03-07 21:24:29","direction":143,"height":13,"radius":172,"speed":1.151352},{"loc_time":1488892963,"location":[119.19862366523,26.05657490478],"create_time":"2017-03-07 21:23:04","direction":166,"height":13,"radius":6,"speed":3.92},{"loc_time":1488892960,"location":[119.19864101868,26.05679869603],"create_time":"2017-03-07 21:23:04","floor":"","radius":40,"speed":2.678138,"direction":172},{"loc_time":1488892929,"location":[119.19861150667,26.057004362948],"create_time":"2017-03-07 21:22:32","direction":99,"height":15,"radius":7,"speed":4.357264},{"loc_time":1488892892,"location":[119.19816922345,26.057069819469],"create_time":"2017-03-07 21:21:31","direction":62,"height":15,"radius":6,"speed":3.42721},{"loc_time":1488892868,"location":[119.19796676469,26.056974278182],"create_time":"2017-03-07 21:21:31","direction":20,"height":16,"radius":7,"speed":4.959726},{"loc_time":1488892821,"location":[119.19774185148,26.056428218507],"create_time":"2017-03-07 21:20:30","floor":"","radius":62.671696,"speed":3.69516,"direction":226},{"loc_time":1488892813,"location":[119.19780100257,26.05647948337],"create_time":"2017-03-07 21:20:30","direction":12,"height":17,"radius":6,"speed":4.326775},{"loc_time":1488892774,"location":[119.19769713749,26.056068461128],"create_time":"2017-03-07 21:19:58","direction":298,"height":19,"radius":4,"speed":4.132053},{"loc_time":1488892721,"location":[119.19823131148,26.055805926437],"create_time":"2017-03-07 21:18:51","direction":284,"height":20,"radius":4,"speed":4.139255}]
     */

    private int status;
    private String message;
    private int total;
    private int size;
    private double distance;
    private int toll_distance;
    private StartPointBean start_point;
    private EndPointBean end_point;
    private List<PointsBean> points;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getToll_distance() {
        return toll_distance;
    }

    public void setToll_distance(int toll_distance) {
        this.toll_distance = toll_distance;
    }

    public StartPointBean getStart_point() {
        return start_point;
    }

    public void setStart_point(StartPointBean start_point) {
        this.start_point = start_point;
    }

    public EndPointBean getEnd_point() {
        return end_point;
    }

    public void setEnd_point(EndPointBean end_point) {
        this.end_point = end_point;
    }

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public class StartPointBean {
        /**
         * longitude : 119.19884391113
         * latitude : 26.056454427666
         * coord_type : 3
         * loc_time : 1488888010
         */

        private double longitude;
        private double latitude;
        private int coord_type;
        private int loc_time;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getCoord_type() {
            return coord_type;
        }

        public void setCoord_type(int coord_type) {
            this.coord_type = coord_type;
        }

        public int getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(int loc_time) {
            this.loc_time = loc_time;
        }
    }

    public class EndPointBean {
        /**
         * longitude : 119.1988231067
         * latitude : 26.056335430387
         * coord_type : 3
         * loc_time : 1488893067
         */

        private double longitude;
        private double latitude;
        private int coord_type;
        private int loc_time;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getCoord_type() {
            return coord_type;
        }

        public void setCoord_type(int coord_type) {
            this.coord_type = coord_type;
        }

        public int getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(int loc_time) {
            this.loc_time = loc_time;
        }
    }

    public  class PointsBean {
        /**
         * loc_time : 1488893067
         * location : [119.1988231067,26.056335430387]
         * create_time : 2017-03-07 21:24:29
         * direction : 143
         * height : 13
         * radius : 172
         * speed : 1.151352
         * floor :
         */

        private int loc_time;
        private String create_time;
        private int direction;
        private int height;
        private double speed;
        private String floor;
        private List<Double> location;

        public int getLoc_time() {
            return loc_time;
        }

        public void setLoc_time(int loc_time) {
            this.loc_time = loc_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }


        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }
    }
}
