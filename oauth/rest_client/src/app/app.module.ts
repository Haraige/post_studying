import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from "@angular/router";

import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {UserstableComponent} from './userstable/userstable.component';
import {HttpClientModule} from "@angular/common/http";
import {CreateuserComponent} from './createuser/createuser.component';
import { NotFoundComponent } from './not-found/not-found.component';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import { HomeComponent } from './home/home.component';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080/auth',
        realm: 'auth-service',
        clientId: 'angular-client',
      },
      loadUserProfileAtStartUp: true,
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',
      },
      bearerExcludedUrls: ['/assets', '/clients/public'],
    });
}

@NgModule({
  declarations: [
    AppComponent,
    UserstableComponent,
    CreateuserComponent,
    NotFoundComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule ,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot([
      {path: "admin", component: UserstableComponent},
      {path: "", component: HomeComponent, pathMatch: "full"},
      {path: "admin/create", component: CreateuserComponent, pathMatch: "full"},
      {path: "admin/edit/:login", component: CreateuserComponent, pathMatch: "full"},
      {path: "404", component: NotFoundComponent, pathMatch: "full"},
      {path: "**", redirectTo: "/404"},
    ]),
    KeycloakAngularModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
