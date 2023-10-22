#include <iostream>
#include <fstream>
#include <string>
#include <stack>
#include <vector>
using namespace std;

stack<int> S;
vector<int> instructions;
long parse_index = 0;

void interpret();

int main() {
    string name = "program.bnk";
    // cout << "Enter input file name" << endl;
    //cin >> name;
    ifstream text(name);

    // PARSING
    char ch;
    int score = 0;
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
    
    for (char c : "Calcolatrice in Beatnik") cout << int(c) << "; ";
    cout << endl;
    //INTERPRETING
    S.push(0);
    while (parse_index < instructions.size()) {
        interpret();
        parse_index++;
    }

    return 0;
}

int get_and_pop() {
    if (S.size() == 0) {
        cout << "ERROR: You are trying to pop an empty stack at " << parse_index << ". Check your poem." << endl;
        exit(-1);
    } else {
        if (S.size() == 1) cout << "WARNING: You are completely emptying the stack with a POP operation at " << parse_index << ". While this may be intentional, it usually means that your code is broken. " << endl;
        int p = S.top(); S.pop();
        return p;
    }
}

void interpret() {
    switch (instructions[parse_index])
    {
    case 5: {
        if (parse_index == instructions.size() - 1) cout << "WARNING: PUSH instruction at the end of the file; will be ignored." << endl;
        else S.push(instructions[++parse_index]);
        break;
    }
    case 6: {
        get_and_pop();
        break;
    }
    case 7: {
        int a = get_and_pop();
        int b = get_and_pop();
        S.push(a + b);
        break;
    }
    case 8: {
        int input;
        cin >> input;
        S.push(input);
        break;
    }
    case 9: { 
        int c = get_and_pop();
        cout << char(c);
        break;
    }
    case 10: {
        int a = get_and_pop();
        int b = get_and_pop();
        S.push(a - b);
        break;
    }
    case 11: {
        int a = get_and_pop();
        int b = get_and_pop();
        S.push(b); S.push(a);
        break;
    }
    case 12: {
        //check if it works
        S.push(S.top());
        break;
    }
    case 13: {
        int old_ind = parse_index;
        if (get_and_pop() == 0) parse_index += instructions[++parse_index];
        if (parse_index >= instructions.size()) cout << "WARNING: N_FORWARD_IF_ZERO went beyond the instruction limit by " << (parse_index - instructions.size() - 1) << " at index " << old_ind <<". While this may be intentional, it could create bugs." << endl;
        break;
    }
    case 14: {
        int old_ind = parse_index;
        if (get_and_pop() != 0) parse_index += instructions[++parse_index];
        if (parse_index >= instructions.size()) cout << "WARNING: N_FORWARD_IFN_ZERO went beyond the instruction limit by " << (parse_index - instructions.size() - 1) << " at index " << old_ind <<". While this may be intentional, it could create bugs." << endl;
        break;
    }
    case 15: {
        int old_ind = parse_index;
        if (get_and_pop() == 0) parse_index -= instructions[++parse_index];
        if (parse_index < 0) cout << "WARNING: N_BACK_IF_ZERO went less than zero by " << (-parse_index) << " at index " << old_ind <<". While this may be intentional, it could create bugs." << endl;
        break;
    }
    case 16: {
        int old_ind = parse_index;
        if (get_and_pop() != 0) parse_index -= instructions[++parse_index];
        if (parse_index < 0) cout << "WARNING: N_BACK_IFN_ZERO went less than zero by " << (-parse_index) << " at index " << old_ind <<". While this may be intentional, it could create bugs." << endl;
        break;
    }
    case 17: {
        exit(0);
        break;
    }
    default:
        break; //NOOP
    }
}

