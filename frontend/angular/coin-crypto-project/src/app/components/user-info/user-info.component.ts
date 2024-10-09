import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; 
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { Wallet } from '../../model/wallet'; // Assuming you have a Wallet model
import { CommonModule } from '@angular/common';
import { WalletDetailsResponse } from '../../model/walletDetailsResponse';

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
  totalWorth: number = 0;
  lastPurchase: Wallet | undefined;

  constructor(private route: ActivatedRoute, private userService: UserService) {

   }

  ngOnInit(): void { 
    this.route.paramMap.subscribe(params => {
      const userId = +params.get('id')!;
      this.userService.getUser(userId).subscribe(
        (data: User) => {
          console.log('Fetched user data:', data);  
          this.user = data;
          this.fetchWallets(userId);
          this.fetchWalletDetails(userId);
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

  fetchWalletDetails(userId: number): void {
    this.userService.getWalletDetails(userId).subscribe(
      (data: WalletDetailsResponse) => {
        console.log('Fetched wallet details:', data);
        this.wallets = data.wallets.sort((a, b) => new Date(b.countTimeStamp).getTime() - new Date(a.countTimeStamp).getTime());
        this.totalWorth = data.totalWorth;
        this.lastPurchase = data.lastPurchase;
      },
      (error) => console.error('Error fetching wallet details', error)
    );
  }

}