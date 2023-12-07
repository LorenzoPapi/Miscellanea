#include <stdio.h>
#include <assert.h>

int main() {
    FILE *fr, *fw;
    int N;

    fr = fopen("input.txt", "r");
    fw = fopen("output.txt", "w");
    assert(1 == fscanf(fr, "%d", &N));
    fprintf(fw, "%.6f\n", 0.25 * N * (N-1));
    fclose(fr);
    fclose(fw);
    return 0;
}
