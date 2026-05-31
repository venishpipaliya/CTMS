import { Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { AddUsersComponent } from './components/admin/add-users/add-users.component';
import { EditUsersComponent } from './components/admin/edit-users/edit-users.component';
import { AdminLayoutComponent } from './components/layouts/admin-layout/admin-layout.component';
import { EmployeeLayoutComponent } from './components/layouts/employee-layout/employee-layout.component';
import { ViewRequestComponent } from './components/employee/view-request/view-request.component';
import { EmployeeDashboardComponent } from './components/employee/employee-dashboard/employee-dashboard.component';
import { NewRequestComponent } from './components/employee/new-request/new-request.component';
import { ManagerLayoutComponent } from './components/layouts/manager-layout/manager-layout.component';
import { ManagerApprovalComponent } from './components/manager/manager-approval/manager-approval.component';
import { FinanceLayoutComponent } from './components/layouts/finance-layout/finance-layout.component';
import { FinanceApprovedComponent } from './components/finance/finance-approved/finance-approved.component';


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

  {
    path: 'manager',
    component: ManagerLayoutComponent,
    children: [
      {path: '', redirectTo: 'manager-approval', pathMatch: 'full' },
      {path: 'manager-approval', component: ManagerApprovalComponent },
    ],
  },

  {
    path: 'finance',
    component: FinanceLayoutComponent,
    children: [
      {path: '', redirectTo: 'finance-approval', pathMatch: 'full' },
      {path: 'finance-approval', component: FinanceApprovedComponent },
    ],
  },
];
