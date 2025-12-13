import { Component,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from '../../services/user';

@Component({
  selector: 'app-user-list',
  imports: [CommonModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit{
  users: any[] = [];

  constructor(private user: User) {}

  ngOnInit() {
    this.user.getUsers().subscribe(data => {
      this.users = data;
    });
  }
  deleteUser(id: number) {
    this.user.deleteUser(id).subscribe(() => {
      this.users = this.users.filter(user => user.id !== id);
    });
  }

}
