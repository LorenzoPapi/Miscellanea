// I just wanted to say, Snaptik.app, if you see this, please enhance your security measures. It was surprisingly easy to reverse engineer the rather ineffective "protection" you're currently employing.
// Author: cxstles on github
// Date: Sep 29th, 2023.

// Initialize variables
let h, u, n, t, e, r;

// Get values from command line arguments
process.argv.forEach(function (val, index) {
    switch (index) {
        case 2:
            h = val;
            break;
        case 3:
            u = parseInt(val);
            break;
        case 4:
            n = val;
            break;
        case 5:
            t = parseInt(val);
            break;
        case 6:
            e = parseInt(val);
            break;
        case 7:
            r = parseInt(val);
            break;
    }
});

// Decode function
function decodeString(d, e, f) {
    const baseChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";
    const charArray = baseChars.split('').slice(0, e);
    const outputChars = baseChars.split('').slice(0, f);

    let j = d.split("").reverse().reduce(function (a, b, c) {
        if (charArray.indexOf(b) !== -1) {
            return a + charArray.indexOf(b) * Math.pow(e, c);
        }
        return a;
    }, 0);

    let k = "";
    while (j > 0) {
        k = outputChars[j % f] + k;
        j = Math.floor(j / f);
    }

    return k || "0";
}

function evalFunction(h, u, n, t, e, r) {
    r = "";

    for (let i = 0, len = h.length; i < len; i++) {
        let s = "";
        while (h[i] !== n[e]) {
            s += h[i];
            i++;
        }
        for (let j = 0; j < n.length; j++) {
            s = s.replace(new RegExp(n[j], "g"), j);
        }
        r += String.fromCharCode(parseInt(decodeString(s, e, 10)) - t);
    }
    return decodeURIComponent(escape(r));
}

console.log(evalFunction(h, u, n, t, e, r));
