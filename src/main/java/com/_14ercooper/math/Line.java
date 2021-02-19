package com._14ercooper.math;

public class Line {

    private Point3 pt1, pt2, dir;

    public Line(Point3 first, Point3 second) {
	pt1 = first;
	pt2 = second;
	dir = pt2.add(pt1.mult(-1));
	dir.normalize();
    }

    public double distanceTo(Point3 point) {
	Point3 closest = closestPoint(point);
	return closest.distance(point);
    }

    public Point3 closestPoint(Point3 other) {
	Point3 v = other.add(pt1.mult(-1));
	double d = v.dot(dir);
	return pt1.add(dir.mult(d));
    }

    public double distanceFromFirst(Point3 linePoint) {
	return pt1.distance(linePoint);
    }
}
