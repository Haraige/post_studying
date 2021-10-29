import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-interp',
  templateUrl: './userstable.component.html',
  styleUrls: ['./userstable.component.css']
})

export class UserstableComponent implements OnInit {

  constructor(public http: HttpClient, public router: Router, private readonly keycloak: KeycloakService) { }

  ngOnInit(): void {
    if (!this.keycloak.isUserInRole("ADMIN")) {
      this.router.navigateByUrl("/")
    }
    this.getAllUsers()
  }
  private baseUrl:string = "http://127.0.0.1:9000/api/admin/"
  public users:Array<TableUser>|undefined

  getAllUsers() {
    this.http.get<Array<ResponseUser>>(this.baseUrl + "users")
      .subscribe(data => {
        this.users = data.map(user => <TableUser> {
          login: user.login,
          email: user.email,
          firstName: user.firstName,
          lastName: user.lastName,
          age: (new Date()).getFullYear() - new Date(user.birthday).getFullYear(),
          role: user.role
        })
      },
      error => {
        console.log(error)
      })
  }

  removeUser(login: string) {
    if (confirm("Are you sure about deleting user with login " + login + "?")) {
      this.http.delete(this.baseUrl + "users/login/" + login).subscribe(() => this.ngOnInit())
    }
  }

}

interface TableUser {
  login: string
  email: string
  firstName: string
  lastName: string
  age: number
  role: string
}

export interface ResponseUser {
  login: string
  email: string
  firstName: string
  lastName: string
  birthday: Date
  role: string
}
