#include <iostream>
#include <fstream>
#include <bitset>
#include <vector>

#define TAPE_SIZE 50000

using namespace std;

bitset<TAPE_SIZE> tape;
vector<int> out_buffer;
vector<int> instructions;
long long parse_index = 0;
int cursor = 0;

// add '-d "DEBUG"' to the compiler options for the debug file 

#ifdef DEBUG
streambuf* stream_buffer_cout = cout.rdbuf();
streambuf* stream_buffer_debug;
#endif

void interpret();

enum codes : char {
    IN = '\1',
    OUT = -12, //0xF4 0x8F 0xBF 0xBE
    PUSH = '1',
    POP = '0',
    RIGHT = '>',
    LEFT = '<',
    FLIP = '-',
    SKIP_IF_ONE = '?',
    AND = 'A',
    OR = 'O',
    NAND = 'N',
    XOR = 'X',
    JUMP_TO_BEGINNING = '|'
};

// SOH10FFFE language specifics and examples: https://esolangs.org/wiki/Soh_supplementary_private_use_area-b_u%2B10fffe

int main(int argc, char * argv[]) {
    string name = argc == 1 ? "program.soh10fffe" : argv[1];
    ifstream text(name);
    
    char ch;
    if (text.is_open())
        while (text >> skipws >> ch)
            instructions.push_back(ch);
    
    text.close();

    #ifdef DEBUG
    ofstream debug_out(name + ".code");
    stream_buffer_debug = debug_out.rdbuf();
    cout.rdbuf(stream_buffer_debug);
    cout << "BASE:\nout_buffer=[]\ncursor=0\nparse=0\n" << endl;
    #endif

    while (parse_index < instructions.size()) interpret();

    #ifdef DEBUG
    cout.rdbuf(stream_buffer_cout);
    #endif
    
    cout << "\n\nTerminated." << endl;
    return 0;
}

void print_out_buffer() {
    for (auto i : out_buffer) cout << i;
}

void interpret() {
    switch (instructions[parse_index]) {
        case IN: {
            string input;
            cout << "Input requested:" << endl;
            cin >> input;
            int temp_cursor = cursor;
            for (char c : input) {
                for (size_t i = 0; i < 8; i++) {
                    tape[temp_cursor++] = c & (1 << (7 - i));
                    if (temp_cursor >= TAPE_SIZE) temp_cursor = 0;
                }
            }
            break;
        }
        case OUT: {
            #ifdef DEBUG
            cout.rdbuf(stream_buffer_cout);
            #endif

            if (out_buffer.size() % 8 != 0) cout << "WARNING: " << (out_buffer.size() % 8) << " will be discarded " << endl;
            char c = 0;
            for (size_t i = 0; i < out_buffer.size(); i++) {
                c |= out_buffer[i];
                if (i % 8 == 7) {
                    cout << c;
                    c = 0;
                } else c <<= 1;
            }
            
            #ifdef DEBUG
            cout.rdbuf(stream_buffer_debug);
            #endif
            break;
        }
        case PUSH:
            out_buffer.push_back(tape[cursor]);
            break;
        case POP:
            tape[cursor] = out_buffer.back();
            out_buffer.pop_back();
            break;
        case RIGHT:
            cursor++;
            if (cursor >= TAPE_SIZE) cursor = 0;
            break;
        case LEFT:
            cursor--;
            if (cursor < 0) cursor = TAPE_SIZE;
            break;
        case FLIP:
            tape.flip(cursor);
            break;
        case SKIP_IF_ONE:
            if (tape[cursor]) parse_index++;
            break;
        case AND:
            tape[cursor] = tape[(cursor - 1) % TAPE_SIZE] & tape[(cursor - 2) % TAPE_SIZE];
            break;
        case OR:
            tape[cursor] = tape[(cursor - 1) % TAPE_SIZE] | tape[(cursor - 2) % TAPE_SIZE];
            break;
        case NAND:
            tape[cursor] = !(tape[(cursor - 1) % TAPE_SIZE] & tape[(cursor - 2) % TAPE_SIZE]);
            break;
        case XOR:
            tape[cursor] = tape[(cursor - 1) % TAPE_SIZE] ^ tape[(cursor - 2) % TAPE_SIZE];
            break;
        case JUMP_TO_BEGINNING:
            parse_index = -1;
            break;
        default:
            break;
    }
    parse_index++;

    #ifdef DEBUG
    cout << instructions[parse_index];
    if (instructions[parse_index] == PUSH || instructions[parse_index] == POP || instructions[parse_index] == OUT) {
        cout << ": out_buffer = ";
        print_out_buffer();
    } else if (instructions[parse_index] == RIGHT || instructions[parse_index] == LEFT) cout << ": cursor = " << cursor << "; tape[cursor] = " << tape[cursor];
    cout << endl;
    #endif    
}