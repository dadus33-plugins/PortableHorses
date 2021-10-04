package net.nordicraft.phorses.utils;

public class Maths {
	
	public static int floor(double num) {
	    int i = (int) num;
	    return (num < i) ? (i - 1) : i;
	}

	public static int round(double num) {
		return floor(num + 0.5d);
	}

	public static int roundLoc(double num) {
		return (int) Math.round(num - 0.5d);
	}

	public static double square(double num) {
		return num * num;
	}
}
