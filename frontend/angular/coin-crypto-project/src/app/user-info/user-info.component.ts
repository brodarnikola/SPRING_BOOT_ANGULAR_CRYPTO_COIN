import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; 
import { UserService } from '../services/user.service';
import { User } from '../model/user';
import { Wallet } from '../model/wallet'; // Assuming you have a Wallet model
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
  wallets: Wallet[] = [];

  constructor(private route: ActivatedRoute, private userService: UserService) {

   }

  ngOnInit(): void {
    // const userId = 1;  
    // this.userService.getUser(userId).subscribe(
    //   (data: User) => {

    //     console.log('Fetched user data:', data);  
    //     this.user = data;
    //     this.fetchWallets(userId);
    //   }, 
    //   (error) => console.error('Error fetching user data', error)
    // );
    this.route.paramMap.subscribe(params => {
      const userId = +params.get('id')!;
      this.userService.getUser(userId).subscribe(
        (data: User) => {
          console.log('Fetched user data:', data);  
          this.user = data;
          this.fetchWallets(userId);
        }, 
        (error) => console.error('Error fetching user data', error)
      );
    });
  }
  
  fetchWallets(userId: number): void {
    this.userService.getWallets(userId).subscribe(
      (data: Wallet[]) => {
        console.log('Fetched wallet data:', data);
        this.wallets = data;
      },
      (error) => console.error('Error fetching wallet data', error)
    );
  }
}