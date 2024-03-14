import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { EventListModule } from './event-list/event-list.module';



@NgModule({
  declarations: [
    HomeComponent,
  ],
  imports: [
    CommonModule,
    EventListModule
    
  ],
  exports: [HomeComponent]
})
export class HomeModule { }
