#include <stdio.h>

/* print Fahrenheit-Celsius table
	for fahr = 0, 20, ..., 300 */
CelsToFahr() {
	float fahr, celsius;
	int lower, upper, step;

	lower = 0;		/* lower limit of table */
	upper = 300;		/* upper limit of table */
	step = 20;		/* step size */

	celsius = lower;
	printf("****************************\n");
	printf("**Celsius-Fahrenheit table**\n");
	printf("****************************\n");
	while (celsius <= upper) {
		fahr = (9.0 / 5.0) * celsius - 32;
		printf(" %3.0f\t\t     %6.1f\n", celsius, fahr);
		celsius = celsius + step;
	}
}