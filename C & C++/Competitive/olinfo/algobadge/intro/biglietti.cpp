#include <iostream>
using namespace std;

#define MIN(a,b) ((a) < (b) ? (a) : (b))
int N, M, A, B;

int compra() {
	int div = N / M, rem = N % M;
    return MIN(div * B + (rem ? MIN(rem * A, B) : MIN(M * A, B) - B), N * A);
}

int main() {
    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    cin >> N >> M >> A >> B;
    cout << compra() << endl;
    return 0;
}
