import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-date-badge',
  templateUrl: './date-badge.component.html',
  styleUrls: ['./date-badge.component.scss'],
})
export class DateBadgeComponent implements OnInit {
  @Input() date: Date = new Date();
  public month: String = '';
  public day: Number = 0;

  constructor() {}

  ngOnInit(): void {
    this.day = this.date.getDay()
    this.month = this.date.toLocaleString('default', { month: 'long' }).substring(0, 3).toUpperCase()
  }
}
