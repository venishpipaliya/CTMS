import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-finance-sidebar',
  standalone: true,
  imports: [RouterLinkActive, RouterLink],
  templateUrl: './finance-sidebar.component.html',
  styleUrl: './finance-sidebar.component.scss'
})
export class FinanceSidebarComponent {
employeeName = 'Venish Pipaliya';
  employeeRole = 'Software Engineer';
}
