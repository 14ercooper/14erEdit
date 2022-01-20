/**
 * This file is part of 14erEdit.
 * 
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.math;

public class Line {

    private final Point3 pt1;
    private final Point3 dir;

    public Line(Point3 first, Point3 second) {
        pt1 = first;
        dir = second.add(pt1.mult(-1));
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
