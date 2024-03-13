import { Component, OnInit } from '@angular/core';
import { Route } from 'src/app/core/constants/route';

@Component({
  selector: 'app-header',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  public RouteEnum = Route;
  constructor() {}

  ngOnInit(): void {}
}
