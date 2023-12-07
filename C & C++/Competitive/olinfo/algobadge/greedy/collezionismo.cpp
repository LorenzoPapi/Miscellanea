#include <cstdio>
#include <cstring>
#include <algorithm>
#include <fstream>
#include <iostream>
#include <numeric>
#include <vector>
#include <utility>

using namespace std;

void solve(int t) {
    int N, K;
    cin >> N >> K;

    vector<int> C(N);
    for (int i = 0; i < N; i++) cin >> C[i];
    
    sort(C.begin(), C.end());
    int risposta = C[N - 1] - C[0];
    for (int i = 0; i < N - 1; i++) C[i] = C[i] - C[i + 1];
    
    sort(C.begin(), C.end());
    for (int i = 0; i < K - 1; i++) risposta += C[i];
    
    cout << "Case #" << t << ": " << risposta << "\n";
}

int main() {
    // se preferisci leggere e scrivere da file
    // ti basta decommentare le seguenti due righe:

    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);

    int T;
    cin >> T;

    for (int t = 1; t <= T; t++) {
        solve(t);
    }

    return 0;
}
