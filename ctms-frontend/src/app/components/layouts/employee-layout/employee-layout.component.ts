import { Component } from '@angular/core';
import { EmployeeSidebarComponent } from '../../employee/employee-sidebar/employee-sidebar.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-employee-layout',
  standalone: true,
  imports: [EmployeeSidebarComponent, RouterOutlet],
  templateUrl: './employee-layout.component.html',
  styleUrl: './employee-layout.component.scss'
})
export class EmployeeLayoutComponent {

}
