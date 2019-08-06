package fourteener.worldeditor.math;

public class Matrix {
	
	private double[][] contents;
	
	public static Matrix zero = new Matrix (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	public static Matrix identity = new Matrix (1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	
	public Matrix (	double a1, double a2, double a3, double a4,
					double b1, double b2, double b3, double b4,
					double c1, double c2, double c3, double c4,
					double d1, double d2, double d3, double d4) {
		double[][] temp = {	{a1, a2, a3, a4},
							{b1, b2, b3, b4},
							{c1, c2, c3, c4},
							{d1, d2, d3, d4}};
		contents = temp;
	}
	
	public Matrix (double[][] matrixContents) {
		contents = matrixContents;
	}
	
	public double[][] getContents () {
		return contents;
	}
	
	public static Matrix transpose (Matrix m) {
		double[][] c = m.getContents();
		double [][] cT = {	{c[0][0], c[1][0], c[2][0], c[3][0]},
							{c[0][1], c[1][1], c[2][1], c[3][1]},
							{c[0][2], c[1][2], c[2][2], c[3][2]},
							{c[0][3], c[1][3], c[2][3], c[3][3]}};
		return new Matrix (cT);
	}
	
	public static Matrix mult (Matrix m, double k) {
		double[][] c = m.getContents();
		double [][] cS = {	{c[0][0] * k, c[0][1] * k, c[0][2] * k, c[0][3] * k},
							{c[1][0] * k, c[1][1] * k, c[1][2] * k, c[1][3] * k},
							{c[2][0] * k, c[2][1] * k, c[2][2] * k, c[2][3] * k},
							{c[3][0] * k, c[3][1] * k, c[3][2] * k, c[3][3] * k}};
		return new Matrix (cS);
	}
	
	public static Vector mult (Matrix m, Vector v) {
		double[] product = new double[4];
		double[][] mC = m.getContents();
		double[] vC = v.getContents();
		product[0] = mC[0][0] * vC[0] + mC[0][1] * vC[1] + mC[0][2] * vC[2] + mC[0][3] * vC[3];
		product[1] = mC[1][0] * vC[0] + mC[1][1] * vC[1] + mC[1][2] * vC[2] + mC[1][3] * vC[3];
		product[2] = mC[2][0] * vC[0] + mC[2][1] * vC[1] + mC[2][2] * vC[2] + mC[2][3] * vC[3];
		product[3] = mC[3][0] * vC[0] + mC[3][1] * vC[1] + mC[3][2] * vC[2] + mC[3][3] * vC[3];
		return new Vector (product);
	}
	
	public static Matrix mult (Matrix m1, Matrix m2) {
		double[][] m1C = m1.getContents();
		double[][] m2C = m2.getContents();
		double[][] product = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				product[i][j] = m1C[j][0] * m2C[0][i] + m1C[j][1] * m2C[1][i] + m1C[j][2] * m2C[2][i] + m1C[j][3] * m2C[3][i];
			}
		}
		return new Matrix(product);
	}
}
