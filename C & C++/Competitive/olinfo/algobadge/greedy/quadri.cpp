#include <numeric>
using namespace std;

int quadri(int N, long long M, int V[]) {
    for (int i = 0; i < N; i++) 
        if (V[i] > M) return 0;

    int B = 0;
    bool found = false;
    long long sum = V[0];
    while (sum <= M && B < N - 1) sum += V[++B];
    if (sum <= M && B == N - 1) return N;
    
    while (!found && B) {
        sum -= V[B];
        found = sum <= M;
        if (found) {
            for (int i = 0; i < N - B; i++) {
                sum += V[B + i] - V[i];
                found &= sum <= M;
            }
            if (found) return B;
            sum += accumulate(V, V + N - B, 0) - accumulate(V + B, V + N, 0);
        }
        B--;
    }
    return 0;
}
