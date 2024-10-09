import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../model/user';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-info', 
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  user: User | undefined;  // user: User | null = null; // Initialize as null or a default user object

  constructor(private userService: UserService) {

   }

  ngOnInit(): void {
    const userId = 1; // Replace with dynamic user ID as needed
    this.userService.getUser(userId).subscribe(
      (data: User) => {

        console.log('Fetched user data:', data);  
        this.user = data;
      }, 
      (error) => console.error('Error fetching user data', error)
    );
  } 
}