#include <iostream>
#include <fstream>
#include <string>
#include <stack>
#include <vector>
using namespace std;
typedef long long ll;

stack<ll> S;
vector<ll> instructions;
ll parse_index = 0;

void interpret();

int main() {
    string name = "program.bnk";
    // cout << "Enter input file name" << endl;
    //cin >> name;
    ifstream text(name);

    // PARSING
    char ch;
    ll score = 0;
    int scrabble[] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    if (text.is_open()) {
        while (text >> noskipws >> ch) {
            if (!isalpha(ch)) {
                if (score > 0) {
                    instructions.push_back(score);
                    score = 0;
                }
            } else score += scrabble[tolower(ch) - 'a'];
        }
    }
    text.close();
    
    //INTERPRETING
    S.push(0);
    while (parse_index < instructions.size()) {
        interpret();
        parse_index++;
    }

    return 0;
}

ll get_and_pop() {
    if (S.size() == 0) {
        cout << endl << "ERROR: POP with an empty stack at " << parse_index << ". Check your poem." << endl;
        exit(-1);
    } else {
        if (S.size() == 1) cout << endl << "WARNING: You are completely emptying the stack with a POP operation at " << parse_index << ". While this may be intentional, it usually means that your code is broken. " << endl;
        ll p = S.top(); S.pop();
        return p;
    }
}

void arg_command(int code) {
    //map code->name
    ll old_index = parse_index++;
    if (parse_index == instructions.size() - 1) cout << endl << "WARNING: " << code << "instruction at the end of the file; will be ignored." << endl;
    else {
        switch (code) {
            case 5:
                S.push(instructions[parse_index]);
                break;
            case 13:
            //CONTINUA DA QUI SCEM
                if (get_and_pop() == 0) parse_index = old_index + instructions[parse_index] - 1;
                if (parse_index >= instructions.size()) cout << endl << "WARNING: N_FORWARD_IF_ZERO went beyond the instruction limit by " << (parse_index - instructions.size() - 1) << " at index " << old_index <<". While this may be intentional, it could create bugs." << endl;
                break;
            case 14:
                if (get_and_pop() != 0) parse_index = old_index + instructions[parse_index] - 1;
                if (parse_index >= instructions.size()) cout << endl << "WARNING: N_FORWARD_IFN_ZERO went beyond the instruction limit by " << (parse_index - instructions.size() - 1) << " at index " << old_index <<". While this may be intentional, it could create bugs." << endl;
                break;
            case 15:
            case 16:
                if ((get_and_pop() == 0 && code == 15) || (get_and_pop() != 0 && code == 16)) parse_index = old_index - instructions[parse_index] - 1;
                if (parse_index < -1) cout << endl << "WARNING: " << code << " went less than zero by"  << (-parse_index + 1) << " at index " << old_index <<". While this may be intentional, it could create bugs." << endl;
                break;
            default:
                break;
        }
    }
}

void interpret() {
    switch (instructions[parse_index]) {
        case 5:
        case 13:
        case 14:
        case 15:
        case 16:
            arg_command(instructions[parse_index]);
            break;
        case 6:
            get_and_pop();
            break;
        case 7:
            S.push(get_and_pop() + get_and_pop());
            break;
        case 8:
            char input;
            cin >> input;
            S.push(input);
            break;
        case 9: 
            cout << char(get_and_pop());
            break;
        case 10:
            S.push(get_and_pop() - get_and_pop());
            break;
        case 11: {
            ll a = get_and_pop();
            ll b = get_and_pop();
            S.push(b); S.push(a);
            break;
        }
        case 12:
            S.push(S.top());
            break;
        case 17:
            exit(0);
            break;
        default:
            break; //NOOP
    }
}

