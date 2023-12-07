import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

  @Input('parentData') public name;
  @Output() public childEvent = new EventEmitter();
  public newName = "LorenzoPapi";
  public message = "Benvenuto, caro LorenzoPapi";
  public person = {
    "firstName": "Mario",
    "lastName": "Rossi"
  }
  
  constructor() { }

  ngOnInit() {
  }

  fireEvent() {
    this.childEvent.emit('Hey LorenzoPapi');
  }

}
