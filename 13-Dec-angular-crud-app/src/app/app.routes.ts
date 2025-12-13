import { Routes } from '@angular/router';
import { UserAdd } from './components/user-add/user-add';
import { UserEdit } from './components/user-edit/user-edit';
import { UserList } from './components/user-list/user-list';

export const routes: Routes = [
    { path: '', component: UserList},
  { path: 'add', component: UserAdd},
  { path: 'edit/:id', component: UserEdit}
];
