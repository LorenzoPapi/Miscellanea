#include <iostream>
#include <vector>
#include <cassert>
#include <cmath>

using namespace std;

int N, M, massimo, area;
bool checkTris(vector<vector<int>> G, int r, int c);

int gioca(vector<vector<int>> G, int corrente, int p = 0) {
    if (p == N * M) return corrente;
    int x = p / M, y = p % M;
    int punti = gioca(G, corrente, p + 1);
    if (massimo < punti) massimo = punti;
    if ((!G[x][y]) && (punti + 1 > p / 2) && (punti + ceil((area - p) * 0.67 + 0.5) > massimo)) {
        G[x][y] = 1;
        if (checkTris(G, x, y)) punti = max(punti, gioca(G, corrente + 1, p + 1));
    }
    return punti;
}

bool checkTris(vector<vector<int>> G, int r, int c) {
    if (N < 3 && M < 3) return true;
    bool cond = false;
    if (r > 1) {
        if (c > 1) cond |= (G[r - 2][c - 2] & G[r - 1][c - 1]);
        cond |= (G[r - 2][c] & G[r - 1][c]);
        if (c < M - 2) cond |= (G[r - 2][c + 2] & G[r - 1][c + 1]);
    }
    if (r > 0 && r < N - 1) {
        cond |= (G[r - 1][c] & G[r + 1][c]);
        if (c > 0 && c < M -1) {
            cond |= (G[r - 1][c - 1] & G[r + 1][c + 1]);
            cond |= (G[r - 1][c + 1] & G[r + 1][c - 1]);
        }
    }
    if (r < N - 2) {
        cond |= (G[r + 1][c] & G[r + 2][c]);
        if (c > 1) cond |= (G[r + 1][c - 1] & G[r + 2][c - 2]);
        if (c < M - 2) cond |= (G[r + 1][c + 1] & G[r + 2][c + 2]);
    }
    if (c > 1)              cond |= (G[r][c - 2] & G[r][c - 1]);
    if (c > 0 && c < M - 1) cond |= (G[r][c - 1] & G[r][c + 1]);
    if (c < M - 2)          cond |= (G[r][c + 1] & G[r][c + 2]);

    return !cond;
}

int main() {
    cin >> N >> M;
    assert(cin.good());
    int start = 0;
    vector<vector<int>> G(N, vector<int>(M));
    for(auto& x : G)
        for(auto& y : x) {
            cin >> y;
            start += y;
        }
    assert(cin.good());
    area = N * M;
    cout << (gioca(G, start) - start) << endl;
}
