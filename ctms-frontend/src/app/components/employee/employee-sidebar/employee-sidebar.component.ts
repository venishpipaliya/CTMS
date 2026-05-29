import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-employee-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './employee-sidebar.component.html',
  styleUrl: './employee-sidebar.component.scss'
})
export class EmployeeSidebarComponent {
  employeeName = 'Venish Pipaliya';
  employeeRole = 'Software Engineer';
}
