#include <stdio.h>

/* print Fahrenheit-Celsius table
	for fahr = 0, 20, ..., 300 */
FahrToCels() {
	float fahr, celsius;
	int lower, upper, step;

	lower = 0;		/* lower limit of table */
	upper = 2000;		/* upper limit of table */
	step = 20;		/* step size */

	fahr = lower;

	while (fahr <= upper) {
		celsius = (5.0 / 9.0) * (fahr - 32.0);
		printf("%3.0f", fahr);	/* %f is floating point; 0 is maximum decimals; 3 is minimum integers */
		printf(" %6.1f\n", celsius);
		fahr = fahr + step;
	}

	/*
		%d -> decimal integer
		%6d -> decimal integer 6 characters wide minimum
		%f -> floating point
		%6f -> floating point 6 characters wide minium
		%.2f -> floating point with 2 decimals
		%6.2f -> omegacombo of those two
		%o for octals
		%x for hex
		%c for character
		%s for string
		%% for %
	*/
}