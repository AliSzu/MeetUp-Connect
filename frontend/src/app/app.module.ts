import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HeaderModule } from './shared/header/header.module';
import { HomeComponent } from './features/home/home.component';
import { ProfileComponent } from './features/profile/profile.component';

import { RouterModule, Routes } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Route } from './core/constants/route';

const routes: Routes = [
  { path: Route.Home, component: HomeComponent },
  { path: Route.Profile, component: ProfileComponent },
  { path: '**', redirectTo: Route.Home, pathMatch: 'full' }
];

@NgModule({
  declarations: [AppComponent, HomeComponent, ProfileComponent],
  imports: [BrowserModule, RouterModule.forRoot(routes), HeaderModule, NgbModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
