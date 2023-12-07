#include <iostream>
#include <vector>

using namespace std;
#define MAX(a,b) ((a > b) ? (a) : (b))
#define MIN(a,b) ((a < b) ? (a) : (b))

int gcd(int a, int b);

int solve(){
    int N;
    cin >> N;
    vector<int> V(N);
    for(int &x: V){
        cin >> x;
    }

	int last = V[0];
	int lcm = 1;
	for (int x : V) {
		if (lcm % x != 0)
			lcm = last * x / gcd(last, x);
		last = lcm;
	}

    return lcm;
}

int gcd(int a, int b) {
	int M = MAX(a, b);
	int m = MIN(a, b);

	if (M % m == 0) return m;
	return (m == 1) ? 1 : gcd(M % m, m);
}

int main() {
    int T;
    cin >> T;
    for (int i = 0; i < T; i++){
        cout << "Case #" << i+1 << ": " << solve() << endl;
    }
    return 0;
}
