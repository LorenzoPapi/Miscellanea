var truepass = 'A1C2';
var number = 7140;

guesspass1()
guesspass2()
guesspass3()
guesspass4()

function guesspass1() {
    for (i = 0; i < number; i++) {
        if (toBase12(i) == truepass) {
            console.warn(i)
            break;
        }
    }
}

function guesspass2() {
    for (i = number; i < (number * 2); i++) {
        if (toBase12(i) == truepass) {
            console.warn(i)
            break;
        }
    }
}

function guesspass3() {
    for (i = (number * 2); i < (number * 3); i++) {
        if (toBase12(i) == truepass) {
            console.warn(i)
            break;
        }
    }
}

function guesspass4() {
    for (i = (number * 3); i <= (number * 4); i++) {
        if (toBase12(i) == truepass) {
            console.warn(i)
            break;
        }
    }
}

function fromBase12(s) {
    var digits = '0123456789ABC';
    var result = 0;
    for (var i=0 ; i<s.length ; i++) {
        var p = digits.indexOf(s[i]);
        if (p < 0) {
            return NaN;
        }
        result += p * Math.pow(digits.length, s.length - i - 1);
    }
    return result;
}

function toBase12(n) {
    if (n === 0) {
        return '0';
    }
    var digits = '0123456789ABC';
    var result = '';
    while (n > 0) {
        result = digits[n % digits.length] + result;
        n = parseInt(n / digits.length, 10);
    }
    return result;
}

function toBase62(n) {
    if (n === 0) {
        return '0';
    }
    var digits = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var result = ''; 
    while (n > 0) {
        result = digits[n % digits.length] + result;
        n = parseInt(n / digits.length, 10);
    }
    return result;
}
  
function fromBase62(s) {
    var digits = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var result = 0;
    for (var i=0 ; i<s.length ; i++) {
        var p = digits.indexOf(s[i]);
        if (p < 0) {
            return NaN;
        }
        result += p * Math.pow(digits.length, s.length - i - 1);
    }
    return result;
}