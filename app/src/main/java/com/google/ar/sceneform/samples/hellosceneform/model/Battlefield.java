package com.google.ar.sceneform.samples.hellosceneform.model;

import android.graphics.Point;

import java.util.ArrayList;

public class Battlefield {
    boolean[][] field;
    final byte size = 10;
    final char[] horizontal = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    final byte[] vertical = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private ArrayList<Ship> ships = new ArrayList<Ship>();

    public boolean addShip(Ship ship) {
        boolean result = false;

        if((ship.start.x < 9 && ship.start.x > 0) && (ship.start.y < 9 && ship.start.y > 0) &&
        (ship.finish.x < 9 && ship.finish.x > 0) && (ship.finish.y < 9 && ship.finish.y > 0))
            result = true;

        if (result) ships.add(ship);

        return result;
    }
    //+ если по короблю, - если мимо/уже было
    public boolean fire(Point pos) {
        boolean result = false;
        /*for(Ship ship: ships){
           if(ship.start.x != ship.finish.x)

            for (int i=0; i<ship.)
        }*/
        if(field[pos.x][pos.y])
        {
            return result;
        }

        return result;
    }

    public boolean isOver() {
        boolean result = true;

        for (Ship ship : ships) {
            if (ship.damage.size() != ship.size()){
                result = false;
                break;
            }
        }

        return result;
    }


    public Battlefield() {
        field = new boolean[size][size];
    }

    public abstract class Ship {
        Point start, finish;
        protected ArrayList<Point> damage = new ArrayList<Point>();

        public Ship(Point start, Point finish) {
            this.start = start;
            this.finish = finish;
        }

        public byte size() {
            return (byte) Math.sqrt(Math.abs(Math.pow(finish.x - start.x, 2.0) +Math.pow(finish.y - start.y, 2.0)));
        }
    }

    public  class OneShip extends Ship {

        public OneShip(Point start, Point finish) {
            super(start, finish);
        }
    }

    public class TwoShip extends Ship {

        public TwoShip(Point start, Point finish) {
            super(start, finish);
        }
    }

    public class ThreeShip extends Ship {
        public ThreeShip(Point start, Point finish) {
            super(start, finish);
        }
    }

    public class FourShip extends Ship {

        public FourShip(Point start, Point finish) {
            super(start, finish);
        }
    }
}
