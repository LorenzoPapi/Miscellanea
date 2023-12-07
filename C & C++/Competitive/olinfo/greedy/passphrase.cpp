/*
 * NOTE: it is recommended to use this even if you don't
 * understand the following code.
 */

#include <stdio.h>
#include <iostream>
#include <string>

using namespace std;

// input data

int main() {
    int N, K;
    string passphrase, soluzione;
//  uncomment the following lines if you want to read/write from files
//  freopen("input.txt", "r", stdin);
//  freopen("output.txt", "w", stdout);

    cin >> N >> K;
    cin >> passphrase;

    // insert your code here

    int i, j, c, maxi;
    int len = N - K;
    for (c = 0, i = 0; c < len; c++) {
        maxi = i;
        for (j = i; j < i + K + 1; j++) {
            if (passphrase[j] > passphrase[maxi]) maxi = j;
        }
        K -= (maxi - i);
        soluzione += passphrase[maxi];
        i = maxi + 1;
    }
    
    cout << soluzione << endl; // print the result
    return 0;
}
