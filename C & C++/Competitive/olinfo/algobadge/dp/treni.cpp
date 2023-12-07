#define MAX(a, b) ((a > b) ? (a) : (b))

int tempo_massimo(int N, int a[], int b[]) {
    if (N == 1) return MAX(a[0], b[0]);
    int points[3];
    points[0] = MAX(a[0], b[0]); points[1] = MAX(points[0] + a[1], b[1]);
    for (int g = 2; g < N; g++) points[g % 3] = MAX(points[(g - 2) % 3] + b[g], points[(g - 1) % 3] + a[g]);
    return points[(N - 1) % 3];
}
