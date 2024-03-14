import { Component, OnInit } from '@angular/core';
import { MeetUpEvent } from 'src/app/core/models/event.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  public dummyEvent: MeetUpEvent = {
    name: "Spacerek fufu",
    description: "Idziemy na spacerek",
    dateFrom: new Date(), // You can set the date here
    dateTo: new Date(), // You can set the date here
    address: "123 Main St"
  };

  public dummyEventList: MeetUpEvent[] = []
  constructor() { }

  ngOnInit(): void {
    for(let i=0; i<10; i++) {
      this.dummyEventList.push(this.dummyEvent)
    }
  }

}
