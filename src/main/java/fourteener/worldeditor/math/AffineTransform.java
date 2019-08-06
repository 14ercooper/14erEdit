package fourteener.worldeditor.math;

import org.bukkit.Location;

public class AffineTransform {
	
	public static Vector getPosition (double x, double y, double z) {
		return new Vector(x,y,z,1);
	}
	
	public static Vector getPosition (Location loc) {
		return new Vector(loc.getX(),loc.getY(),loc.getZ(),1);
	}
	
	public static Matrix getTranslateMatrix (double xT, double yT, double zT) {
		double[][] c = {	{1, 0, 0, 0},
							{0, 1, 0, 0},
							{0, 0, 1, 0},
							{xT, yT, zT, 1}};
		return new Matrix (c);
	}
	
	public static Matrix getScaleMatrix (double xS, double yS, double zS) {
		double[][] c = {	{xS, 0, 0, 0},
							{0, yS, 0, 0},
							{0, 0, zS, 0},
							{0, 0, 0, 1}};
		return new Matrix (c);
	}
	
	public static Matrix getShearMatrix (double xy, double yz, double xz) {
		double yx = 0;
		double zy = 0;
		double zx = 0;
		double[][] c = {	{1, yx, zx, 0},
							{xy, 1, zy, 0},
							{xz, yx, 1, 0},
							{0, 0, 0, 1}};
		return new Matrix (c);
	}
	
	public static Matrix getReflectionMatrix (double a, double b, double c, double d) {
		double[][] co = {	{1-(2*a*a), -2*a*b, -2*a*c, -2*a*d},
							{-2*a*b, 1-(2*b*b), -2*b*c, -2*b*d},
							{-2*a*c, -2*b*c, 1-(2*c*c), -2*c*d},
							{0, 0, 0, 1}};
		return new Matrix (co);
	}
	
	public static Matrix getRotationXMatrix (double theta) {
		theta *= 0.0174533;
		double[][] c = {	{1, 0, 0, 0},
							{0, Math.cos(theta), -1*Math.sin(theta), 0},
							{0, Math.sin(theta), Math.cos(theta), 0},
							{0, 0, 0, 1}};
		return new Matrix (c);
	}
	
	public static Matrix getRotationYMatrix (double theta) {
		theta *= 0.0174533;
		double[][] c = {	{Math.cos(theta), 0, Math.sin(theta), 0},
							{0, 1, 0, 0},
							{-1*Math.sin(theta), 0, Math.cos(theta), 0},
							{0, 0, 0, 1}};
		return new Matrix (c);
	}
	
	public static Matrix getRotationZMatrix (double theta) {
		theta *= 0.0174533;
		double[][] c = {	{Math.cos(theta), -1*Math.sin(theta), 0, 0},
							{Math.sin(theta), Math.cos(theta), 0, 0},
							{0, 0, 1, 0},
							{0, 0, 0, 1}};
		return new Matrix (c);
	}
}
