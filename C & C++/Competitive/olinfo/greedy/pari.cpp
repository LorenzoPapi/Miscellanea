#include <iostream>
using namespace std;

int main() {
    int N;
    cin >> N;
    cout << ((N & 1) ? "dis" : "") << "pari" << endl;
    return 0;
}