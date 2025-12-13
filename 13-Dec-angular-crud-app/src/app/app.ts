import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserAdd } from "./components/user-add/user-add";
import { UserEdit } from "./components/user-edit/user-edit";
import { UserList } from "./components/user-list/user-list";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,UserAdd, UserEdit, UserList],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('angular-crud-app');
}
