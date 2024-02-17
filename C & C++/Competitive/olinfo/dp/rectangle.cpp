#include <fstream>
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
    // uncomment the two following lines if you want to read/write from files
    // ifstream cin("input.txt");
    // ofstream cout("output.txt");

    int N;
    cin >> N;
    
    vector<int> S(N);
    for (int i = 0; i < N; ++i)
        cin >> S[i];
    
    long long A = 0;
    
    sort(S.begin(), S.end());
    long long max1 = 0, max2 = 0;
    for (int i = N - 1; i > 0; i--) {
        if (S[i] == S[i - 1]) {
            if (S[i] > max1) {
                max1 = S[i];
                i--;
            } else if (max1 > 0 && S[i] > max2) {
                max2 = S[i];
                break;
            }
        }
    }
    
    cout << max1*max2 << endl;

    return 0;
}