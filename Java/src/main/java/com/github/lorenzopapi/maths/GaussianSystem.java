package com.github.lorenzopapi.maths;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class GaussianSystem {
	public static void main(String[] args) {
		int exponent = 3;
		Matrix g = gaussian(exponent);
		System.out.println(Arrays.toString(g.lastColumn()));
	}

	public static Matrix gaussian(int exponent) {
		Matrix m = new Matrix(exponent + 1, exponent + 2);
		for (int i = 0; i < m.rows; i++)
			m.insertRowAt(i, createDoubleArr(i + 1, exponent));
		m.gaussianAlgorithm();
		return m;
	}

	public static double[] createDoubleArr(int base, int exponent) {
		double[] coefficients = new double[exponent + 2];
		for (int i = 0; i < exponent + 1; i++)
			coefficients[i] = Math.pow(base, exponent + 1 - i);
		coefficients[exponent + 1] = sumOfPowers(base, exponent);
		return coefficients;
	}

	public static long sumOfPowers(int n, int exponent) {
		long sum = 0;
		for (int i = 1; i <= n; i++) sum += Math.pow(i, exponent);
		return sum;
	}

	public static class Matrix {
		private final int rows, cols;
		private final double[][] array;

		public Matrix(int r, int c) {
			this.rows = r;
			this.cols = c;
			this.array = new double[r][c];
		}

		public void insertRowAt(int index, double[] r) {
			double[] rounded = new double[r.length];
			for (int i = 0; i < r.length; i++) {
				BigDecimal bd = new BigDecimal(Double.toString(r[i]));
				bd = bd.setScale(8, RoundingMode.HALF_UP);
				rounded[i] = bd.doubleValue();
			}
			this.array[index] = rounded;
		}

		public Fraction[] lastColumn() {
			Fraction[] col = new Fraction[rows];
			for (int i = 0; i < rows; i++) col[i] = new Fraction(array[i][cols - 1]);
			return col;
		}

		public void mulRowAndInsert(int index, double mul) {
			this.insertRowAt(index, mulRow(index, mul));
		}

		public double[] mulRow(int index, double mul) {
			double[] r = this.array[index];
			double[] n = new double[r.length];
			for (int i = 0; i < r.length; i++) n[i] = r[i] * mul;
			return n;
		}

		public void addRows(int index, double[] toAdd) {
			double[] row = this.array[index];
			for (int i = 0; i < row.length; i++) row[i] += toAdd[i];
			this.insertRowAt(index, row);
		}

		public void gaussianHelper(int row) {
			for (int i = 1; i < this.rows; i++) {
				double mul = this.array[i][row - 1];
				if (mul != 0 && mul != 1) addRows(i, mulRow(row - 1, -mul));
			}
			if (array[row][row] != 1) mulRowAndInsert(row, 1/array[row][row]);
			addRows(0, mulRow(row, -array[0][row]));
		}

		public void gaussianAlgorithm() {
			System.out.println(this);

			for (int r = 1; r < this.rows; r++) gaussianHelper(r);

			for (int i = 1; i < this.rows; i++) {
				double mul = this.array[i][rows-1];
				if (mul != 0 && mul != 1) {
					addRows(i, mulRow(rows-1, -mul));
					System.out.format("Iteration %d, %d:%n" + this, rows, i);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder output = new StringBuilder();
			for (int i=0; i<this.rows; i++) {
				for (int j=0; j<this.cols; j++)
					output.append(String.format(Double.toString(array[i][j]), "%-10.1f")).append(" ");
				output.append("\n");
			}
			return output.toString();
		}
	}

	public static class Fraction {

		long numerator;
		long denominator;

		Fraction(long n, long d) {
			numerator = n;
			denominator = d;
			reduce();
		}

		Fraction(double d) {
			BigDecimal bd = new BigDecimal(Double.toString(d));
			bd = bd.setScale(4, RoundingMode.HALF_EVEN);
			d = bd.doubleValue();
			int i = (String.valueOf(d)).split("\\.")[1].length();
			denominator = (long) Math.pow(10, i);
			numerator = (long) (d * denominator);
			reduce();
		}
		
		private long gcd(long a, long b) {
			while (b != 0) {
				long t = b;
				b = a % b;
				a = t;
			};
			return a;
		}

		private void reduce() {
			long n = Math.abs(numerator), d = denominator;
			long gcd = gcd(n, d);
			if (gcd != 0) {
				numerator /= gcd;
				denominator /= gcd;
			}
		}

		public String toString() {
			if (numerator == 0) return "0";
			if (numerator == denominator) return "1";
			return numerator + "/" + denominator;
		}
	}
}
