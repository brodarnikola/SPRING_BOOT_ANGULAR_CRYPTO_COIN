import { Routes } from '@angular/router';
import { UserInfoComponent } from './components/user-info/user-info.component';
import { UserListComponent } from './components/user-list/user-list.component';

export const routes: Routes = [
    {path: '', component: UserListComponent},
    {path: 'user-info/:id', component: UserInfoComponent}
];
