#include <LiquidCrystal.h>

const int BUTTON=7, LED=13, BUZZ=9, RS=12, EN=11, D4=5, D5=4, D6=3, D7=2, space_mul = 10, unit = 60;
const char keys[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?";
const LiquidCrystal lcd(RS, EN, D4, D5, D6, D7);
const String codes[] = {
  ".-", "-...", "-.-.", "-..", ".", "..-.",
  "--.", "....", "..", ".---", "-.-", ".-..",
  "--", "-.", "---", ".--.", "--.-", ".-.",
  "...", "-", "..-", "...-", ".--", "-..-",
  "-.--", "--..", "-----", ".----", "..---", "...--",
  "....-", ".....", "-....", "--...", "---..", "----."
};

int lcd_index = 0, last_time = 0, curr_button = LOW, prev_button = LOW;
char lcd_string[32];
bool space = true;
String input = "";

void setup() {
  for (int i = 0; i < 32; i++) lcd_string[i] = ' ';
  pinMode(BUTTON, 0x3);
  pinMode(LED, OUTPUT);
  pinMode(BUZZ, OUTPUT);
  // Serial.begin(9600);
  lcd.begin(16, 2);
}

void loop() {
  curr_button = digitalRead(BUTTON);
  digitalWrite(LED, curr_button);
  if (curr_button != prev_button) {
    if (curr_button == LOW) {
      noTone(BUZZ);
      int press_time = millis() - last_time;
      input += (press_time < 3 * unit) ? '.' : '-';
    } else {
      space = false;
    }
    last_time = millis();
  } else {
    if (curr_button == LOW) {
      int unpress_time = millis() - last_time;
      if (unpress_time > space_mul * unit && !space) {
        add_c(' ');
        space = true;
      } else if (unpress_time > 3 * unit && input != "") {
        add_c(keys[decode()]);
      }
    } else {
      tone(BUZZ, 700);
    }
  }
  prev_button = curr_button;
  delay(10);
}

int decode() {
  int code = 36;
  for (int i = 0; i < 36; i++) {
    if (codes[i] == input) {
      code = i;
      break;
    }
  }
  input = "";
  return code;
}

void add_c(char c) {
  //Add character
  if (lcd_index < 32) lcd_string[lcd_index++] = c;
  else {
    for (int i = 1; i < 32; i++) {
      lcd_string[i-1] = lcd_string[i];
    }
    lcd_string[31] = c;
  }
  
  //Print string
  char row1[16], row2[16];
  strncpy(row1, lcd_string, 16);
  strncpy(row2, lcd_string + 16, 16);
  
  lcd.setCursor(0, 0);
  lcd.print(row1);
  lcd.setCursor(0, 1);
  lcd.print(row2);
  if (lcd_index < 32) lcd.setCursor(lcd_index%16, lcd_index/16);
  else lcd.setCursor(15, 1);
  lcd.cursor();
}