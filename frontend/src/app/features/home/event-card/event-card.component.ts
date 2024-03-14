import { Component, Input, OnInit } from '@angular/core';
import { MeetUpEvent } from 'src/app/core/models/event.model';

@Component({
  selector: 'app-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
})
export class EventCardComponent implements OnInit {
  @Input() event: MeetUpEvent = {} as MeetUpEvent;

  constructor() {}

  ngOnInit(): void {}
}
