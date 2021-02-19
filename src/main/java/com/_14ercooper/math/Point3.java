package com._14ercooper.math;

public class Point3 {

    private double x, y, z;

    public Point3(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public void normalize() {
	double invMag = 1.0 / magnitude();
	x *= invMag;
	y *= invMag;
	z *= invMag;
    }

    public double distance(Point3 other) {
	double xD = x - other.x;
	double yD = y - other.y;
	double zD = z - other.z;
	return Math.sqrt((xD * xD) + (yD * yD) + (zD * zD));
    }

    public double magnitude() {
	return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public Point3 add(Point3 other) {
	double xN = x + other.x;
	double yN = y + other.y;
	double zN = z + other.z;
	return new Point3(xN, yN, zN);
    }

    public Point3 mult(double magnitude) {
	return new Point3(x * magnitude, y * magnitude, z * magnitude);
    }

    public double dot(Point3 other) {
	return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public double getZ() {
	return z;
    }
}
