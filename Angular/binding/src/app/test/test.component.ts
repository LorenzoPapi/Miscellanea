import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

  public name = "Lorenzo";
  public siteUrl = window.location.href;
  public myId = "testId";
  public isDisabled = false;
  public successClass = "text-success"
  public hasError = true;
  public isSpecial = true;
  public messageClasses =
  {
    "text-success": !this.hasError,
    "text-danger": this.hasError,
    "text-special": this.isSpecial
  }
  public highlightColor = "orange";
  public titleStyles =
  {
    color: "blue",
    fontStyle: "italic"
  }
  public greeting = "";
  public newGreeting = "";
  public newName = "";

  constructor() { }

  ngOnInit() {
  }

  greetUser() {
    return "Ciao " + this.name;
  }

  onClick(event) {
    console.log(event);
    this.greeting = 'Benvenuto ' + this.name;
  }

  onNewClick(event) {
    console.log(event);
    this.newGreeting = event.type;
  }

  logMessage(value){
    console.log(value);
  }
  
}
