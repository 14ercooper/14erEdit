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

public class Point3 {

    private double x, y, z;

    public Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3(Point3 original) {
        this.x = original.x;
        this.y = original.y;
        this.z = original.z;
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

    public Point3 sub(Point3 other) {
        double xN = x - other.x;
        double yN = y - other.y;
        double zN = z - other.z;
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

    @Override
    public String toString() {
        return "Point3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static Point3 fromString(String string) {
        if (!string.startsWith("Point3")) {
            return null;
        }
        String minusStart = string.substring(7);
        String minusEnd = minusStart.substring(0, minusStart.length() - 1);
        String[] split = minusEnd.split(",");
        double x = Double.parseDouble(split[0].trim().substring(2));
        double y = Double.parseDouble(split[1].trim().substring(2));
        double z = Double.parseDouble(split[2].trim().substring(2));
        return new Point3(x, y, z);
    }
}
