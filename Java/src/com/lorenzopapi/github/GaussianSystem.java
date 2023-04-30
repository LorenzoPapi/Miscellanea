package com.lorenzopapi.github;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class GaussianSystem {
	public static void main(String[] args) {
		int k = 10;
		Matrix g = gaussian(k);
		System.out.println(Arrays.toString(g.lastColumn()));
	}

	public static Matrix gaussian(int k) {
		Matrix m = new Matrix(k+1, k+2);
		for (int i = 0; i < k+1; i++) {
			m.insertRowAt(i, createDoubleArr(i+1, k));
		}
		m.gaussianAlgorithm();
		return m;
	}

	public static double[] createDoubleArr(int n, int k) {
		double[] coefficients = new double[k+2];
		for (int i=k+1; i > 0; i--) {
			coefficients[k+1-i] = Math.pow(n, i);
		}
		coefficients[k+1] = sumOfPowers(n, k);
		return coefficients;
	}

	public static long sumOfPowers(int n, int k) {
		long sum = 1;
		for (int i = 2; i <= n; i++)
			sum += Math.pow(i, k);
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
				bd = bd.setScale(8, RoundingMode.HALF_EVEN);
				rounded[i] = bd.doubleValue();
			}
			this.array[index] = rounded;
		}

		public Fraction[] lastColumn() {
			Fraction[] col = new Fraction[rows];
			for (int i = 0; i < rows; i++) {
				col[i] = new Fraction(array[i][cols-1]);
			}
			return col;
		}

		public void mulRowAndInsert(int index, double mul) {
			this.insertRowAt(index, mulRow(index, mul));
		}

		public double[] mulRow(int index, double mul) {
			double[] r = this.array[index];
			double[] n = new double[r.length];
			for (int i = 0; i < r.length; i++) {
				n[i] = r[i] * mul;
			}
			return n;
		}

		public void addRows(int index, double[] toAdd) {
			double[] row = this.array[index];
			for (int i = 0; i < row.length; i++) {
				row[i] = row[i] + toAdd[i];
			}
			this.insertRowAt(index, row);
		}

		public void gaussianHelper(int k) {
			for (int i = 1; i < this.rows; i++) {
				double mul = this.array[i][k-1];
				if (mul != 0 && mul != 1) {
					addRows(i, mulRow(k-1, -mul));
				}
			}

			if (array[k][k] != 1) {
				mulRowAndInsert(k, 1/array[k][k]);
			}

			addRows(0, mulRow(k, -array[0][k]));
		}

		public void gaussianAlgorithm() {
			System.out.println(this);

			for (int i = 1; i < this.rows; i++) {
				gaussianHelper(i);
			}

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
				for (int j=0; j<this.cols; j++) {
					output.append(String.format(Double.toString(array[i][j]), "%-10.1f")).append(" ");
				}
				output.append("\n");
			}
			return output.toString();
		}
	}

	public static class Fraction {

		long numerator;
		long denominator;

		Fraction(int n, int d) {
			numerator = n;
			denominator = d;
			reduce();
		}

		Fraction(double d) {
			BigDecimal bd = new BigDecimal(Double.toString(d));
			bd = bd.setScale(4, RoundingMode.HALF_EVEN);
			d = bd.doubleValue();
<<<<<<< Updated upstream
			int i = (String.valueOf(d)).split("\\.")[1].length();
=======
			int i = ("" + d).split("\\.")[1].length();
>>>>>>> Stashed changes
			denominator = (long) Math.pow(10, i);
			numerator = (long) (d * denominator);
			reduce();
		}

		private void reduce() {
<<<<<<< Updated upstream
			// find the larger between the numerator and denominator
=======
			// find the larger of the numerator and denominator
>>>>>>> Stashed changes
			long n = numerator, d = denominator, largest;
			if (numerator < 0) {
				n = -numerator;
			}
			largest = Math.max(n, d);

			// find the largest number that divide the numerator and
			// denominator evenly
			long gcd = 0;
			for (long i = largest; i >= 2; i--) {
				if (numerator % i == 0 && denominator % i == 0) {
					gcd = i;
					break;
				}
			}

			// divide the largest common denominator out of numerator, denominator
			if (gcd != 0) {
				numerator /= gcd;
				denominator /= gcd;
			}
		}

		public String toString() {
			if (numerator == 0)
				return "0";
			return numerator + "/" + denominator;
		}
	}
}
