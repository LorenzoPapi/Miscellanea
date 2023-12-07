#include <stdio.h>

double convert(float fahr);

FToCWithFunction() {
	float fahr;
	int lower, upper, step;

	lower = 0;
	upper = 2000;
	step = 20;

	fahr = 0;

	while (fahr <= upper) {
		printf("%3.0f", fahr);
		printf(" %6.1f\n", convert(fahr));
		fahr = fahr + step;
	}
}

double convert(float fahr) {
	return (5.0 / 9.0) * (fahr - 32.0);
}