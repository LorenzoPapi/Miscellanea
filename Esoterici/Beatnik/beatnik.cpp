#include <iostream>
#include <fstream>
#include <stack>
#include <vector>
using namespace std;
typedef long long ll;

stack<ll> S;
vector<ll> words;
ll parse_index = 0;

void interpret();

enum codes : ll {
    PUSH = 5,
    POP = 6,
    ADD = 7,
    IN = 8,
    OUT = 9,
    SUB = 10,
    SWAP = 11,
    DUP = 12,
    FORWARD_N_IF_ZERO = 13,
    FORWARD_N_IFN_ZERO = 14,
    BACKWARD_N_IF_ZERO = 15,
    BACKWARD_N_IFN_ZERO = 16,
    STOP = 17
};

// Beatnik language specifics and examples: https://esolangs.org/wiki/Beatnik

int main(int argc, char * argv[]) {
    ifstream text(argc == 1 ? "program.bnk" : argv[1]);
    char ch;
    ll score = 0;
    int scrabble[] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    if (text.is_open()) {
        while (text >> noskipws >> ch) {
            if (isalpha(ch)) score += scrabble[tolower(ch) - 'a'];
            else if (score > 0) {
                words.push_back(score);
                score = 0;
            }
        }
    }
    text.close();
    
    while (parse_index < (ll) words.size()) interpret();

    cout << "\n\nTerminated." << endl;

    return 0;
}

ll get_and_pop() {
    if (S.size() == 0) {
        cout << endl << "ERROR: POP with an empty stack at " << parse_index << ". Check your poem." << endl;
        exit(-1);
    } else {
        ll p = S.top(); S.pop();
        return p;
    }
}

void interpret() {
    if (parse_index < 0) parse_index = 0;
    if (parse_index >= words.size()) {
        cout << "ERROR: index is beyond limits" << endl;
        exit(-1);
    }
    switch (words[parse_index]) {
        case PUSH:
            S.push(words[++parse_index]);
            break;
        case POP:
            get_and_pop();
            break;
        case ADD:
            S.push(get_and_pop() + get_and_pop());
            break;
        case IN: {
            char input;
            cin >> input;
            S.push(input);
            break;
        }
        case OUT: 
            cout << char(get_and_pop());
            break;
        case SUB:
            S.push(get_and_pop() - get_and_pop());
            break;
        case SWAP: {
            ll a = get_and_pop();
            ll b = get_and_pop();
            S.push(b); S.push(a);
            break;
        }
        case DUP:
            S.push(S.top());
            break;
        case FORWARD_N_IF_ZERO:
            parse_index++;
            if (get_and_pop() == 0) {
                parse_index += words[parse_index];
                return;
            }
            break;
        case FORWARD_N_IFN_ZERO:
            parse_index++;
            if (get_and_pop() != 0) {
                parse_index += words[parse_index];
                return;
            }
            break;
        case BACKWARD_N_IF_ZERO:
            parse_index++;
            if (get_and_pop() == 0) {
                parse_index -= words[parse_index] + 1;
                return;
            }
            break;
        case BACKWARD_N_IFN_ZERO:
            parse_index++;
            if (get_and_pop() != 0) {
                parse_index -= words[parse_index] + 1;
                return;
            }
            break;
        case STOP:
            exit(0);
            return;
        default:
            break; //NOOP
    }
    parse_index++;
}

