package com.github.lorenzopapi.maths;

//haha funny math go brrrrrrrrrrrrrrr
public class EpicMathHelper {

	private static final String equation = "x = 2 + 3";

	public static void main(String[] args) {
		System.out.println(add(10, 2.2, 20, 0.1, 5D/2D, 0x1));
		System.out.println(subtract(10, 2.2, 20, 0.1, 5D/2D, 0x1));
		System.out.println(multiply(10, 2.2, 20, 0.1, 5D/2D, 0x1));
		System.out.println(divide(10, 2.2, 20, 0.1, 5D/2D, 0x1));
		String localE = equation.replaceAll("\\s", "");
		System.out.println(localE);
		solveEasily(localE);
	}

	private static void solveEasily(String eq) {
		char[] member1 = eq.split("=")[0].toCharArray();
		char[] member2 = eq.split("=")[1].toCharArray();

		for (int i = 0; i < member2.length; i++) {
			if (member2[i] == '+' || member2[i] == '-' || member2[i] == '*' || member2[i] == '/') {
				double operand1 = Double.parseDouble(String.valueOf(member2[i - 1]));
				double operand2 = Double.parseDouble(String.valueOf(member2[i + 1]));
				double result = 0;
				if (member2[i] == '+') {
					result = add(operand1, operand2);
				} else if (member2[i] == '-') {
					result = subtract(operand1, operand2);
				} else if (member2[i] == '*') {
					result = multiply(operand1, operand2);
				} else if (member2[i] == '/') {
					result = divide(operand1, operand2);
				}
				System.out.println(result);
			}
		}
	}

	private static double add(double... operands) {
		double result = 0;
		for (double operand : operands)
			result += operand;
		return result;
	}

	private static double subtract(double... operands) {
		double result = 0;
		for (double operand : operands)
			result -= operand;
		return result;
	}

	private static double multiply(double... operands) {
		double result = 1;
		for (double operand : operands)
			result *= operand;
		return result;
	}

	private static double divide(double... operands) {
		double result = 1;
		for (double operand : operands)
			result /= operand;
		return result;
	}
}
