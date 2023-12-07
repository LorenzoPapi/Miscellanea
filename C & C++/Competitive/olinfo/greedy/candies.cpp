// NOTE: it is recommended to use this even if you don't understand the following code.

#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
	// uncomment the following lines if you want to read/write from files
	// ifstream cin("input.txt");
	// ofstream cout("output.txt");

    int N;
    cin >> N;
    long long ans = N;

    vector<int> S(N);
    for (int i = 0; i < N; i++) cin >> S[i];
    sort(S.begin(), S.end());

    for (int i = 1; i < N; i++) {
        if (S[i] != S[i-1]) ans += N - i;
    }
    
    cout << ans << endl;
    return 0;
}