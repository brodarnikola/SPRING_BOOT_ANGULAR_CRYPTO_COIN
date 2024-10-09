import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { provideHttpClient } from '@angular/common/http'; 

import { AppComponent } from './app.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes'; 


@NgModule({
  declarations: [
    AppComponent,
    UserInfoComponent
  ],
  imports: [
    BrowserModule, 
    RouterModule.forRoot(routes)  
  ],
  providers: [
    provideHttpClient() // Use this instead of HttpClientModule, 
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }