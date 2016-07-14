package edu.uncc.wins.gestureslive;

/**
 * Created by jbandy3 on 6/22/2015.
 */
public class Coordinate {
    private Double x, y, z;
    private Double magnitude;
    public Coordinate(Double x, Double y, Double z){
        this.x = x;
        this.y = y;
        this.z = z;
        magnitude = (Math.sqrt((x*x) + (y*y) + (z*z)) -1);
    }

    public Double[] toArray(){
        Double[] toReturn = {this.x, this.y,this.z};
        return toReturn;
    }

    public String toString(){
        return "x: " + x + "\ny: " + y + "\nz: " + z;
    }

    public String toShortString(){
        return String.format("x: %.2f", x) + String.format("\ny: %.2f",y) + String.format("\nz: %.2f", z);
    }

    public Double getMagnitude(){
        return magnitude;
    }
}
