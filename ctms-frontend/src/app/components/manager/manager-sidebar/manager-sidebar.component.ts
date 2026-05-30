import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-manager-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './manager-sidebar.component.html',
  styleUrl: './manager-sidebar.component.scss'
})
export class ManagerSidebarComponent {
employeeName = 'Venish Pipaliya';
  employeeRole = 'Software Engineer';
}
