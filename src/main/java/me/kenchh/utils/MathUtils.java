package me.kenchh.utils;

public class MathUtils {

    public static double round(double toRound) {
        return (double) (Math.round(toRound * 10000)) / 10000;
    }

}
