import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FinanceApprovedComponent } from '../../finance/finance-approved/finance-approved.component';
import { FinanceSidebarComponent } from '../../finance/finance-sidebar/finance-sidebar.component';

@Component({
  selector: 'app-finance-layout',
  standalone: true,
  imports: [FinanceSidebarComponent, RouterOutlet],
  templateUrl: './finance-layout.component.html',
  styleUrl: './finance-layout.component.scss'
})
export class FinanceLayoutComponent {

}
