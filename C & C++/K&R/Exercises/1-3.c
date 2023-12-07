#include <stdio.h>

/* print Fahrenheit-Celsius table
	for fahr = 0, 20, ..., 300 */
FahrToCels() {
	float fahr, celsius;
	int lower, upper, step;

	lower = 0;		/* lower limit of table */
	upper = 300;		/* upper limit of table */
	step = 20;		/* step size */

	fahr = lower;
	printf("****************************\n");
	printf("**Fahrenheit-Celsius table**\n");
	printf("****************************\n");
	while (fahr <= upper) {
		celsius = (5.0 / 9.0) * (fahr - 32.0);
		printf(" %3.0f\t\t     %6.1f\n", fahr, celsius);
		fahr = fahr + step;
	}
}