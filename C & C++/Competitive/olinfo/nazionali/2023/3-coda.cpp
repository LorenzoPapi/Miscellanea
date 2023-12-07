#include <iostream>
#include <utility>
#include <vector>
using namespace std;

vector<int> cucina(int N, int K, int X, vector<int> H);

int main() {
    // se preferisci leggere e scrivere da file ti basta decommentare le seguenti due righe:
    // freopen("input.txt", "r", stdin);
    // freopen("output.txt", "w", stdout);

    int N, K, X;
    cin >> N >> K >> X;

    vector<int> H(N);
    for (int& h : H) {
        cin >> h;
    }

    vector<int> res = cucina(N, K, X, move(H));
    for (int r : res) {
        cout << r << ' ';
    }
    cout << endl;

    return 0;
}

#include <vector>
#include <algorithm>
using namespace std;

vector<int> cucina(int N, int K, int X, vector<int> H) {
    vector<int> R(X, 0);
    vector<int> humans(X, 0);

    for (int h : H) humans[h]++;

    R[X - 1] = min(humans[X - 1], 1);
    // int coda = 0, start = X - 2;
    // while (start >= 0) {
    //     if (humans[start]) {
    //         coda += max(humans[start], K);
    //         if (coda > 0) {
    //             coda--;
    //             R[start]++;
    //         }
    //         R
    //     } else R[start] = R[start+1];
    //     start--;
        
    // }
    for (int start = X - 2; start >= 0; start--) R[start] = min(min(humans[start], K) + R[start + 1] - (? 1 : 0), X - start);

    return R;
}