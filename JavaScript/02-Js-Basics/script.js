/**************************** 
*Variables and data types
*/

/*var firstName = 'John';
console.log(firstName);

var lastName = 'Smith';
var age = 28;

var fullAge = true;
console.log(fullAge);

var job;
console.log(job);

job = "teacher";
console.log(job);

// Variable naming rules
var _3years = 3;
var johnMark = 'John and MArk';
var if = 23;
*/



/**************************** 
* Variables mutation and type coercion
*/
/*
var firstName = 'John';
var age = 28;

// Type coercion
console.log(firstName + ' ' + age);

var job, isMarried;
job = 'teacher';
isMarried = false;

console.log(firstName + ' is a ' + age + ' years old ' + job + '. Is he married? ' + isMarried);

// Variable mutation
age = 'twenty eight';
job = 'driver';

alert(firstName + ' is a ' + age + ' years old ' + job + '. Is he married? ' + isMarried);

var lastName = prompt('What is his last Name?');
console.log(firstName + ' ' + lastName);
*/



/**************************** 
* Basic operators
*/
/*
var now, yearJohn, yearMark, ageJohn, ageMark;
now = 2020;
ageJohn = 28;
ageMark = 23;

// Math operators
yearJohn = now - ageJohn;
yearMark = now - ageMark;

console.log(yearJohn);

console.log(now + 2);
console.log(now * 2);
console.log(now / 10);


// Logical operators
var johnOlder = ageJohn > ageMark;
console.log(johnOlder);


// typeof operator
console.log(typeof johnOlder);
console.log(typeof ageJohn);
console.log(typeof 'Mark is older than John');
var x;
console.log(typeof x);
*/



/**************************** 
* Operator precedence
*/ 
/*
var now = 2020;
var yearJohn = 1989;
var fullAge = 18;

// Multiple operators
var isFullAge = now - yearJohn >= fullAge; // true
console.log(isFullAge);

// Grouping
var ageJohn = now - yearJohn;
var ageMark = 35;
var average = (ageJohn + ageMark) / 2;
console.log(average);

// Multiple assignments
var x, y;
x = y = (3 + 5) * 4 - 6; // 8 * 4 - 6 // 32 - 6 // 26
console.log(x, y);


// More operators
x *= 2; // x = x * 2;
x += 10; // x = x + 10;
x++; // x = x + 1;
x--; // x = x - 1;
*/





/**************************** 
* CODING CHALLENGE 1
*/ 

/*
Mark and John are trying to compare their BMI (Body Mass Index), which is calculated using the forumla: BMI = mass / height^2 = mass / (height * height). (mass in kg and height in meter).

1. Store Mark's and John's mass and height in variables.
2. Calculate both their BMIs
3. Create a boolean variable containing information about whether Mark has a higher BMI than John.
4. Print a string to the console containing the variable from step 3. (Something like "Is Mark's BMI higher than John's? true")

GOOD LUCK :)
*/ 

/*
var massMark, massJohn, heightMark, heightJohn, BMIJohn, BMIMark, isMarkBMIHigher;
massMark = 90;
massJohn = 80;
heightMark = 1.70;
heightJohn = 1.75;
BMIJohn = massJohn / (heightJohn * heightJohn);
BMIMark = massMark / (heightMark * heightMark);
isMarkBMIHigher = BMIMark > BMIJohn;
console.log("Is Mark's BMI higher than John's? " + isMarkBMIHigher);
*/





/**************************** 
* If / else statement
*/

/*
var firstName = 'John';
var civilStatus = 'single';

if (civilStatus === 'married') {
    console.log(firstName + ' is married!');
} else {
    console.log(firstName + ' will hopefully marry soon :)')
}


var isMarried = true;
if (isMarried) {
    console.log(firstName + ' is married!');
} else {
    console.log(firstName + ' will hopefully marry soon :)')
}

var massMark, massJohn, heightMark, heightJohn, BMIJohn, BMIMark;
massMark = 90;
massJohn = 80;

heightMark = 1.70;
heightJohn = 1.75;

BMIJohn = massJohn / (heightJohn * heightJohn);
BMIMark = massMark / (heightMark * heightMark);

if (BMIMark > BMIJohn) {
    console.log("Mark's BMI is higher than John's.")
} else {
    console.log("John's BMI is higher than Mark's.")
}


/**************************** 
* Boolean logic
*/
/* 
var firstName = 'John';
var age = 16;

if (age < 13) {
    console.log(firstName + " is a boy.");
} else if (age >= 13 && age < 20) { 
    console.log(firstName + " is a teenager.")
} else if (age >= 20 && age < 30) { 
    console.log(firstName + " is a young man.")
} else {
    console.log(firstName + " is a man.");
}
*/



/**************************** 
* The Ternary Operator and Switch Statements
*/
/*
var firstName = 'John';
var age = 16;

// Ternary operator
age >= 18 ? console.log(firstName + " drinks beer") : console.log(firstName + " drinks juice");

var drink = age >= 18 ? "beer" : "juice";

console.log(drink);

//Switch statement
var job = 'teacher';
switch(job) {
    case 'teacher':
    case 'instructor':
        console.log(firstName + ' teaches kids how to code.');
        break;
    case 'driver':
        console.log(firstName + ' drives an uber in Lisbon.');
        break;
    case 'designer':
        console.log(firstName + ' designs beautiful websites.');
        break;
    default: 
        console.log(firstName + ' doesn\'t work');
}

switch(true) {
    case age < 13:
        console.log(firstName + " is a boy.");
        break;
    case age >= 13 && age < 20:
        console.log(firstName + " is a teenager.");
        break;
    case age >= 20 && age < 30:
        console.log(firstName + " is a young man.");
        break;
    default:
        console.log(firstName + " is a man.");
}
*/

/**************************** 
* Truthy and Falsy values and equality operators
*/

// falsy values: undefined, null, 0, '', NaN
// truthy values: NOT falsy values
/*
var height;

height = 23;

if (height || height === 0) {
    console.log("Defined");
} else {
    console.log("Undefined");
}

// Equality operators
if (height == '23') {
    console.log("The == does type coercion");
}
*/



/**************************** 
* CODING CHALLENGE 2
*/

/* John and Mike both play basketball in different teams. In the latest 3 games, John's team scored 89, 120 and 103 points, while Mike's team scored 116, 94 and 123 points.

1. Calculate the average score for each team
2. Decide which teams wind in average (highest average score), and print the winner to the console. Also include the average score in the output.
3. Then change the scores to show different winners. Don't forget to take into account there might be a draw (the same average score)

4. EXTRA: Mary also plays basketball, and her team scored 97, 134 and 105 points. Like before, log the average winner to the coonsole. HINT: you will need the && ioeratir to take the decision
5. Like before, change the scores and keep in mind draws

GOOD LUCK :D
*/

/*
var averageJohn, averageMike;

averageJohn = (89 + 120 + 103) / 3;
averageMike = (116 + 94 + 123) / 3;


if (averageJohn > averageMike) {
    console.log("John is the winner! Average score: " + averageJohn);
} else if (averageMike > averageJohn) {
    console.log("Mike is the winner! Average score: " + averageMike);
} else {
    console.log("It's a draw!");
}


var averageMary;

averageMary = (97 + 134 + 105) / 3;

if (averageJohn > averageMike && averageJohn > averageMary) {
    console.log("John is the winner! Average score: " + averageJohn);
} else if (averageMike > averageJohn && averageMike > averageMary) {
    console.log("Mike is the winner! Average score: " + averageMike);
} else if (averageMary > averageJohn && averageMary > averageMike) {
    console.log("Mary is the winner! Average score: " + averageMary);
} else {
    console.log("It's a draw!");
}
*/



/**************************** 
* Functions
*/

/*
function calculateAge(birthYear) {
    return 2020 - birthYear;
}

var ageJohn = calculateAge(1990);
var ageMike = calculateAge(1948);
var ageJane = calculateAge(1969);

console.log(ageJohn, ageMike, ageJane);


function yearsUntilRetirement(year, firstName) {
    var age = calculateAge(year);
    var retirement = 65 - age;
    if (retirement > 0) {
        console.log(firstName + " retires in " + retirement + " years");
    } else {
        console.log(firstName + " is already retired");
    }
}

yearsUntilRetirement(1990, "John");
yearsUntilRetirement(1948, "Mike");
yearsUntilRetirement(1969, "Jane");
*/



/**************************** 
* Functions Statements and Expressions
*/

//Function declaration
//function whatDoYouDo(job, firstName) {}

// Function expression
/*var whatDoYouDo = function(job, firstName) {
    switch(job) {
        case "teacher":
            return firstName + " teaches";
        case "driver":
            return firstName + " drives";
        case "designer":
            return firstName + " designs";
        default:
            return firstName + " does something else";
    }
}

console.log(whatDoYouDo("teacher", "John"));
console.log(whatDoYouDo("designer", "Jane"));
console.log(whatDoYouDo("retired", "Mark"));
*/


/**************************** 
* Arrays
*/

/*
//Initialize new array
var names = ["John", "Mark", "Jane"];
var years = new Array(1990, 1969, 1948);

console.log(names);
console.log(names.length);

// Mutate array data
names[1] = "Ben";
names[names.length] = "Mary";
console.log(names);

// Different data types
var john = ["John", "Smith", 1990, "teacher", false];

john.push("blue");
john.unshift("Mr.");
console.log(john);

john.pop();
john.pop();
john.shift();
console.log(john);

console.log(john.indexOf(23));


var isDesigner = john.indexOf("designer") === -1 ? "John is NOT a designer" : "John IS a designer";
console.log(isDesigner);
*/


/*****************************
* CODING CHALLENGE 3
*/

/*
John and his family went on a holiday and went to 3 different restaurants. The bills were $124, $48 and $268.

To tip the waiter a fair amount, John created a simple tip calculator (as a function). He likes to tip 20% of the bill when the bill is less than $50, 15% when the bill is between $50 and $200, and 10% if the bill is more than $200.

In the end, John would like to have 2 arrays:
1) Containing all three tips (one for each bill)
2) Containing all three final paid amounts (bill + tip).

(NOTE: To calculate 20% of a value, simply multiply it with 20/100 = 0.2)

GOOD LUCK 😀
*/

/*
function tipCalculator(bill) {
    if (bill < 50) {
        return (20 / 100) * bill; 
    }
    if (bill <= 200 && bill >= 50) {
        return (15 / 100) * bill; 
    }
    if (bill > 200) {
        return (10 / 100) * bill; 
    }
}

var tips = [tipCalculator(124), tipCalculator(48), tipCalculator(268)];
var finalAmounts = [tipCalculator(124) + 124, tipCalculator(48) + 48, tipCalculator(268) + 268];
*/


/*****************************
* Object and properties
*/

/*
// Object literal
var john = {
    firstName: "John",
    lastName: "Smith",
    birthYear: 1990,
    family: ["Jane", "Mark", "Bob", "Emily"],
    job: "teacher",
    isMarried: false
};
console.log(john.firstName);
console.log(john["lastName"]);
var x = "birthYear";
console.log(john[x]);

john.job = "designer";
john["isMarried"] = true;
console.log(john);

//new Object sintax
var jane = new Object();
jane.firstName = "Jane";
jane.birthYear = 1969;
jane["lastName"] = "Smith";
console.log(jane);
*/


/*****************************
* Objects and methods
*/

/*
var john = {
    firstName: "John",
    lastName: "Smith",
    birthYear: 1990,
    family: ["Jane", "Mark", "Bob", "Emily"],
    job: "teacher",
    isMarried: false,
    calcAge: function () {
        this.age = 2020 - this.birthYear;
    }
};

john.calcAge();
console.log(john);
*/

/*****************************
* CODING CHALLENGE 4
*/

/*
Let's remember the first coding challenge where Mark and John compared their BMIs. Let's now implement the same functionality with objects and methods.
1. For each of them, create an object with properties for their full name, mass, and height
2. Then, add a method to each object to calculate the BMI. Save the BMI to the object and also return it from the method.
3. In the end, log to the console who has the highest BMI, together with the full name and the respective BMI. Don't forget they might have the same BMI.

Remember: BMI = mass / height^2 = mass / (height * height). (mass in kg and height in meter).

GOOD LUCK 😀
*/

/*
var john = {
    firstName: "John",
    lastName: "Smith",
    mass: 80,
    height: 1.70,
    calcBMI: function () {
        john.BMI = this.mass / (this.height * this.height);
        return john.BMI;
    }
}

var mark = {
    firstName: "Mark",
    lastName: "Smith",
    mass: 90,
    height: 1.75,
    calcBMI: function () {
        mark.BMI = this.mass / (this.height * this.height);
        return mark.BMI;
    }
}

john.calcBMI();
mark.calcBMI();

if (mark.BMI < john.BMI) {
    console.log(john.firstName + " " + john.lastName + "'s BMI(" + john.BMI + ") is greater than " + mark.firstName + " " + mark.lastName + "'s one(" + mark.BMI + ")");
} else if (mark.BMI > john.BMI) {
    console.log(john.firstName + " " + john.lastName + "'s BMI(" + john.BMI + ") is smaller than " + mark.firstName + " " + mark.lastName + "'s one(" + mark.BMI + ")");
} else {
    console.log(john.firstName + " " + john.lastName + "'s BMI is equal to " + mark.firstName + " " + mark.lastName + "'s one(" + john.BMI + ")");
}
*/


/*****************************
* Loops and iteration
*/

// For loop
/*
for (var i = 0; i < 10; i++) {
    console.log(i);
}

// i = 0, 0 < 10 true, log i to console, i++
// i = 1, 1 < 10 true, log i to console, i++
// ...
// i = 9, 9 < 10 true, log i to console, i++
// i = 10, 10 < 10 FALSE, exit the loop

var john = ["John", "Smith", 1990, "teacher", false, "blue"];


for (var i = 0; i < john.length; i++) {
    console.log(john[i]);
}

// While loop 
var i = 0;
while(i < john.length) {
    console.log(john[i]);
    i++;
}


// continue and break statements
var john = ["John", "Smith", 1990, "teacher", false, "blue"];

for (var i = 0; i < john.length; i++) {
    if (typeof john[i] !== "string") continue;
    console.log(john[i]);
}

for (var i = 0; i < john.length; i++) {
    if (typeof john[i] !== "string") break;
    console.log(john[i]);
}


// Looping backwards
for (var i = john.length - 1; i >= 0; i--) {
    console.log(john[i]);
}
*/


/*****************************
* CODING CHALLENGE 5
*/

/*
Remember the tip calculator challenge? Let's create a more advanced version using everything we learned!

This time, John and his family went to 5 different restaurants. The bills were $124, $48, $268, $180 and $42.
John likes to tip 20% of the bill when the bill is less than $50, 15% when the bill is between $50 and $200, and 10% if the bill is more than $200.

Implement a tip calculator using objects and loops:
1. Create an object with an array for the bill values
2. Add a method to calculate the tip
3. This method should include a loop to iterate over all the paid bills and do the tip calculations
4. As an output, create 1) a new array containing all tips, and 2) an array containing final paid amounts (bill + tip). HINT: Start with two empty arrays [] as properties and then fill them up in the loop.


EXTRA AFTER FINISHING: Mark's family also went on a holiday, going to 4 different restaurants. The bills were $77, $375, $110, and $45.
Mark likes to tip 20% of the bill when the bill is less than $100, 10% when the bill is between $100 and $300, and 25% if the bill is more than $300 (different than John).

5. Implement the same functionality as before, this time using Mark's tipping rules
6. Create a function (not a method) to calculate the average of a given array of tips. HINT: Loop over the array, and in each iteration store the current sum in a variable (starting from 0). After you have the sum of the array, divide it by the number of elements in it (that's how you calculate the average)
7. Calculate the average tip for each family
8. Log to the console which family paid the highest tips on average

GOOD LUCK 😀
*/

/*var johnRestaurants = {
    totalPaid: [],
    tips: [],
    bills: [124, 48, 268, 180, 42],
    calcTip () {
        for (i = 0; i < this.bills.length; i++) {
            bill = this.bills[i];
            if (bill < 50) {
                this.tips[i] = 0.2 * bill;
            } else if (bill >= 50 && bill <= 200) {
                this.tips[i] = 0.15 * bill;
            } else if (bill > 200) {
                this.tips[i] = 0.1 * bill;
            }
        }
    },
    calcPay () {
        for (i = 0; i < this.bills.length; i++) {
            this.totalPaid[i] = this.bills[i] + this.tips[i];
        }
    }
}

johnRestaurants.calcTip();
johnRestaurants.calcPay();
console.log("John Tips: " + johnRestaurants.tips);
console.log("John Payments: " + johnRestaurants.totalPaid);

var markRestaurants = {
    tips: [],
    bills: [77, 375, 110, 45],
    calcTip () {
        for (i = 0; i < this.bills.length; i++) {
            bill = this.bills[i];
            if (bill < 100) {
                this.tips[i] = 0.2 * bill;
            } else if (bill >= 100 && bill <= 300) {
                this.tips[i] = 0.15 * bill;
            } else if (bill > 300) {
                this.tips[i] = 0.1 * bill;
            }
        }
    },
    totalPaid: [],
    calcPay () {
        for (i = 0; i < this.bills.length; i++) {
            this.totalPaid[i] = this.bills[i]; + this.tips[i];
        }
    }
}

markRestaurants.calcTip();
markRestaurants.calcPay();

function averageTip(arrayName) {
    var sum = 0;
    for (i = 0; i < arrayName.length; ++i) {
        sum += arrayName[i];
    }
    average = sum / arrayName.length;
    return average;
}

var averageJohn = averageTip(johnRestaurants.tips);
var averageMark = averageTip(markRestaurants.tips);

console.log("On average, " + (averageJohn > averageMark ? "John " : "Mark ") + "has payed the highest tips")
*/


function averageTime() {                                        //dichiaro la funzione
    var begin = 0
    var end = 0
    var times = 10000                                           //quante volte ripetere il conteggio
    var sum = 0                                                 //creo la somma
    for (i = 0; i < times; i++) {                               //per "times" volte, fai tutto questo
        begin = performance.now()                               //inizio tempo
        1 + 1
        end = performance.now()                                 //fine tempo
        sum += end - begin                                      //aggiungo alla somma
    }
    average = sum / times                                       //calcolo la media
    console.warn("Ci ha messo in media " + average + " ms.")    //mostro il risultato
}

averageTime()                                                   //chiamo la funzione
