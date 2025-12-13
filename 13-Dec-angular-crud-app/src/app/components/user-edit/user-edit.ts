import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { User } from '../../services/user';

@Component({
  selector: 'app-user-edit',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
})
export class UserEdit implements OnInit{
  form!: FormGroup;
  id = 1;
  constructor(
    private fb: FormBuilder,
    private userService: User
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: [''],
      email: ['']
    });

    this.userService.getUserById(this.id).subscribe((data: any) => {
      this.form.patchValue(data);
    });
  }
  updateUser() {
    this.userService.updateUser(this.id, this.form.value)
      .subscribe(() => {
        alert('User updated');
      });
  }

}
