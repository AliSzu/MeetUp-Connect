import { Component, Input, OnInit } from '@angular/core';
import { MeetUpEvent } from 'src/app/core/models/event.model';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  @Input() eventList: MeetUpEvent[] = []

  constructor() { }

  ngOnInit(): void {
  }

}
