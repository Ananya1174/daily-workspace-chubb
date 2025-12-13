import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../services/user';

@Component({
  selector: 'app-user-add',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-add.html',
  styleUrl: './user-add.css',
})
export class UserAdd {

  user = {
    name: '',
    email: ''
  };

  constructor(private userService: User) {}

  addUser() {
    this.userService.addUser(this.user).subscribe(() => {
      alert('User added');
      this.user = { name: '', email: '' }; 
    });
  }
}
