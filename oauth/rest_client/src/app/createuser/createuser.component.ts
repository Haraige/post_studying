import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ResponseUser} from "../userstable/userstable.component";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";
import {formatDate} from "@angular/common";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-createuser',
  templateUrl: './createuser.component.html',
  styleUrls: ['./createuser.component.css']
})
export class CreateuserComponent implements OnInit {
  public operationType: string|undefined
  public userLogin: string|undefined
  public foundedUser: ResponseUser|undefined
  public roles: Array<ResponseRole>|undefined
  private baseUrl:string = "http://127.0.0.1:9000/api/admin/"
  public formUser:CreateEditUser|undefined

  constructor(private currentRoute: ActivatedRoute,
              private router: Router,
              private http: HttpClient,
              private keycloak: KeycloakService) {
    this.currentRoute.url.subscribe(value => this.operationType = value[1].path.charAt(0).toUpperCase() + value[1].path.slice(1))
    this.currentRoute.params.subscribe(params => this.userLogin = params['login'])
  }

  ngOnInit(): void {
    if (!this.keycloak.isUserInRole("ADMIN")) {
      this.router.navigateByUrl("/")
    }
    this.getUserByLogin()
    this.getAllRoles()
    this.formUser = CreateEditUser.emptyConstructor()
  }

  getAllRoles() {
    this.http.get<Array<ResponseRole>>(this.baseUrl + "roles")
      .subscribe(data => {
          this.roles = data
        },
        (error:HttpErrorResponse) => {
          if (error.status === 401) {
            this.router.navigateByUrl("/")
          }
        })
  }

  getUserByLogin() {
    if (this.userLogin === undefined) {
      return
    }
    this.http.get<ResponseUser>(this.baseUrl + "users/login/" + this.userLogin)
      .subscribe(data => {
          this.formUser = new CreateEditUser(data.login, "", "", data.email, data.firstName,
            data.lastName, formatDate(data.birthday, "yyyy-MM-dd", 'en-US'), data.role)
        },
        error => {
          console.log(error)
        })
  }

  createOrUpdateUser(userForm:NgForm) {
    if (this.operationType === "Create") {
      this.http.post<CreateEditUser>(this.baseUrl + "users", this.formUser).subscribe(
        data => {
          console.log("Successful created user " + data.login)
        },
        (error:HttpErrorResponse) => {
          userForm.controls[error.error['field']].setErrors({message: error.error['message']})
        },
        () => {
          this.router.navigateByUrl("/admin")
        }
      )
    } else {
      this.http.put<CreateEditUser>(this.baseUrl + "users/login/" + this.formUser?.login, this.formUser).subscribe(
        data => {
          console.log("Successful update user with login", data.login)
        },
        (error:HttpErrorResponse) => {
          userForm.controls[error.error['field']].setErrors({message: error.error['message']})
        },
        () => {
          this.router.navigateByUrl("/admin")
        }
      )
    }
    console.log(this.formUser)
  }
}

interface ResponseRole {
  name: string
}

class CreateEditUser {
  login: string
  password: string
  repeatPassword: string
  email: string
  firstName: string
  lastName: string
  birthday: string
  role: string

  constructor(login: string, password: string, repeatPassword: string, email: string, firstName: string, lastName: string, birthday: string, role: string) {
    this.login = login;
    this.password = password;
    this.repeatPassword = repeatPassword;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.role = role;
  }

  public static emptyConstructor():CreateEditUser {
    return new CreateEditUser("", "", "", "", "", "", formatDate(new Date(), "yyyy-MM-dd", 'en-US') , "")
  }
}
