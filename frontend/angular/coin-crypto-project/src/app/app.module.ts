import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { provideHttpClient } from '@angular/common/http'; 

import { AppComponent } from './app.component';
import { UserListComponent } from './user-list/user-list.component';  
import { UserInfoComponent } from './user-info/user-info.component';
import { ExchangeRateAnalyticsComponent } from './exchange-rate-analytics/exchange-rate-analytics.component';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes'; 

import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    UserListComponent,
    UserInfoComponent,
    ExchangeRateAnalyticsComponent
  ],
  imports: [
    BrowserModule, 
    RouterModule.forRoot(routes),
    FormsModule 
  ],
  providers: [
    provideHttpClient() // Use this instead of HttpClientModule, 
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }