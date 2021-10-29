import { Component, OnInit } from '@angular/core';
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public keycloak:KeycloakService) { }
  public username:String = "anonymous"

  ngOnInit(): void {
    this.keycloak.isLoggedIn().then(() => this.username = this.keycloak.getUsername()).catch(() => console.log())
  }

}
