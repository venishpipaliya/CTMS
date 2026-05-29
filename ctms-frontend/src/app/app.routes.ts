import { Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { AddUsersComponent } from './components/admin/add-users/add-users.component';
import { EditUsersComponent } from './components/admin/edit-users/edit-users.component';
import { AdminLayoutComponent } from './components/layouts/admin-layout/admin-layout.component';
import { EmployeeLayoutComponent } from './components/layouts/employee-layout/employee-layout.component';
import { ViewRequestComponent } from './components/employee/view-request/view-request.component';
import { EmployeeDashboardComponent } from './components/employee/employee-dashboard/employee-dashboard.component';
import { NewRequestComponent } from './components/employee/new-request/new-request.component';

export const routes: Routes = [
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: 'admin-dashboard', pathMatch: 'full' },
      { path: 'admin-dashboard', component: AdminDashboardComponent },
      { path: 'add-users', component: AddUsersComponent },
      { path: 'edit-users', component: EditUsersComponent },
    ],
  },

  {
    path: 'employee',
    component: EmployeeLayoutComponent,
    children: [
      {path: '', redirectTo: 'employee-dashboard', pathMatch: 'full' },
      {path: 'employee-dashboard', component: EmployeeDashboardComponent },
      {path: 'view-request', component: ViewRequestComponent },
      {path: 'new-request', component: NewRequestComponent},
    ],
  },
];
