import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {
  
  public displayName = false;
  public newDisplayName = true;
  public color = "orange";
  public colors = ["red", "blue", "green", "yellow"];

  constructor() {}

  ngOnInit() {
  }

}
