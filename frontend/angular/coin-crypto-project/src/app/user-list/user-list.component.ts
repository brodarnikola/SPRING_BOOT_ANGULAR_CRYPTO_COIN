import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = []; // Adjust the type based on your User model

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe((data: User[]) => {
      this.users = data;
    });
  }

  goToUserDetail(userId: number): void {
    this.router.navigate(['/user-info', userId]);
  }
}
