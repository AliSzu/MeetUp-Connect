import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from './event-card.component';
import { DateBadgeComponent } from '../date-badge/date-badge.component';


@NgModule({
  declarations: [EventCardComponent, DateBadgeComponent],
  imports: [
    CommonModule
  ],
  exports: [EventCardComponent, DateBadgeComponent]
})
export class EventCardModule { }
