import { Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { AddUsersComponent } from './components/admin/add-users/add-users.component';
import { EditUsersComponent } from './components/admin/edit-users/edit-users.component';
import { AdminLayoutComponent } from './components/layouts/admin-layout/admin-layout.component';

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
];
