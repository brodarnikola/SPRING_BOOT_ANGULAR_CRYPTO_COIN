import { Routes } from '@angular/router';
import { UserInfoComponent } from './user-info/user-info.component';

export const routes: Routes = [
    {path: '', component: UserInfoComponent},
    {path: 'user-info', component: UserInfoComponent}
];
