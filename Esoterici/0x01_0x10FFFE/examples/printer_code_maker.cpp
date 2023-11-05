#include <iostream>
#include <fstream>
#include <string>

// Takes a string as input
// Returns a SOH-0x10FFFE program that prints the input string

using namespace std;

int main() {
    cout << "Enter your text:" << endl;
    string input;
    getline(cin, input);
    ofstream output("print_string.soh10fffe");
    cout.rdbuf(output.rdbuf());

    cout << "-";
    bool one = true;
    for (char c : input) {
        for (size_t i = 0; i < 8; i++) {
            bool is_bit_on = c & (1 << (7 - i));
            if (is_bit_on && !one) {
                one = !one;
                cout << "<";
            } else if (!is_bit_on && one) {
                one = !one;
                cout << ">";
            } 
            cout << "1";
        }
    }
    cout << "\U0010FFFE";
}