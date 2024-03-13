import { Component, OnInit } from '@angular/core';
import { Route } from 'src/app/core/constants/route';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public RouteEnum = Route;
  constructor() {}

  ngOnInit(): void {}
}
