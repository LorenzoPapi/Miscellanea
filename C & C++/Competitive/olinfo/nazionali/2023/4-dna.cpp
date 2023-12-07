#include <cstring>
#include <iostream>
#include <string>

using namespace std;

string analizza(int N);

static string secret;
static size_t cnt;

bool test(string qry) {
    cnt += 1;
    return secret.find(qry) != string::npos;
}

int main() {
    cin.exceptions(ios::failbit | ios::badbit);

    // se preferisci leggere e scrivere da file ti basta decommentare le seguenti due righe:
    // freopen("input.txt", "r", stdin);
    // freopen("output.txt", "w", stdout);

    cin >> secret;

    cnt = 0;
    auto ans = analizza(secret.size());

    if (ans == secret) {
        cout << "Risposta corretta!\n" << cnt << " chiamate a test." << endl;
    } else {
        cout << "Risposta sbagliata!" << endl;
    }

    return 0;
}

#include <string>

using namespace std;

bool test(string T);

string analizza(int N) {
    bool has_zero = test("0");
    bool has_one = has_zero ? test("1") : true;
    if (!has_zero) return string(N, "1");
    else if (!has_one) return string(N, "0");
    
     
    test("0");
    test("00");
    test("1");
    test("11");
    test("010101");
    return "101010";
}