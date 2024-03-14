import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventListComponent } from './event-list.component';
import { EventCardModule } from '../event-card/event-card.module';



@NgModule({
  declarations: [EventListComponent],
  imports: [
    CommonModule,
    EventCardModule
  ],
  exports: [EventListComponent]
})
export class EventListModule { }
