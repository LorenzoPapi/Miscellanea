package com.github.lorenzopapi.misc;

public class Matrix {

	private final int rows;
	private final int columns;
	private final double[][] data;

	//Matrix rows x columns of 0s
	private Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new double[rows][columns];
	}

	private static double sarrusDeterminant(Matrix m) {
		if (m.rows != m.columns || m.rows != 3)
			throw new NumberFormatException("The matrix m isn't quadratic!");

		double p1 = m.data[0][0] * m.data[1][1] * m.data[2][2];
		double p2 = m.data[0][1] * m.data[1][2] * m.data[2][0];
		double p3 = m.data[0][2] * m.data[1][0] * m.data[2][1];

		double s2 = m.data[0][0] * m.data[1][2] * m.data[2][1];
		double s1 = m.data[0][1] * m.data[1][0] * m.data[2][2];
		double s3 = m.data[0][2] * m.data[1][1] * m.data[2][0];

		System.out.format("%+f, %+f, %+f, %+f, %+f, %+f%n", p1, p2, p3, s1, s2, s3);
		return (p1 + p2 + p3 - s1 - s2 - s3);
	}

	private Matrix(double[][] data) {
		rows = data.length;
		columns = data[0].length;
		this.data = new double[rows][columns];
		for (int i = 0; i < rows; i++)
			System.arraycopy(data[i], 0, this.data[i], 0, columns);
	}

	private String printMatrix() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < columns; i++) {
			builder.append("\n| ");
			for (int j = 0; j < rows; j++)
				builder.append(String.format("%+f", data[j][i])).append(" ");
			builder.append("|");
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return "Matrix:" +
				"\nrows=" + rows +
				"\ncolumns=" + columns +
				"\ndata=" + printMatrix() +
				"\n";
	}

	public static void main(String[] args) {
		Matrix m = new Matrix(new double[][]{
				{1, 1, 0},
				{2, 5, 3},
				{1, 0, 2}
		});
		System.out.println(m);
		System.out.println("D = " + sarrusDeterminant(m));
	}
}
