// NOTE: it is recommended to use this even if you don't understand the following code.

#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
    // uncomment the following lines if you want to read/write from files
    // ifstream cin("input.txt");
    // ofstream cout("output.txt");

    int N, X, i, ans = 0;
    cin >> N >> X;

    vector<int> L(N);
    for (i = 0; i < N; i++) {
        cin >> L[i];
    }
    sort(L.begin(), L.end());
    
    for (i = 0; i < N - 1; i+=2) {
        if (L[i] + L[i + 1] > X) break;
        ++ans;
    }
    ans += N - i;
    
    // cout << endl;
    // for (int i = 0; i < L.size(); i++)
    //     cout << L[i] << " ";
    // cout << endl;
    // cout << mid << endl;


    cout << ans << endl; // print the result
    return 0;
}
