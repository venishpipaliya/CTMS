import { Component, getPlatform, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminServices } from '../../services/admin/admin.services';
import { CommonModule } from '@angular/common';
import { UserRole } from '../../enum/role';

@Component({
  selector: 'admin-dashboard',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  userList: any[] = [];


  CurrentPage = 1;
  usersPerPage = 10;

  searchTerm = '';
  sortOrder: 'asc' | 'desc' = 'asc';

  constructor(private adminServices: AdminServices) {}

  ngOnInit() {
    this.getAllUsers();
  }

  get TotalEmployees(): number {
    return this.userList.length;
  }

  get TotalManagers(): number {
    return this.userList.filter(user => user.role === UserRole.MANAGER).length;
  }

  get TotalDepartments(): number {
    const departments = new Set(this.userList.map(user => user.department));
    return departments.size;
  }

  getAllUsers() {
    this.adminServices.getAllUse().subscribe({
      next: (res: any) => { this.userList = res.data; console.log(this.userList); },
      error: (err) => console.error('getAllUsers error', err)
    });
  }

  toggleSort() {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
  }


  get filteredAndSortedUsers() {

  const result = this.userList.filter(user => {

    const fullName =
      `${user.firstName || ''} ${user.lastName || ''}`.toLowerCase();

    return (
      fullName.includes(this.searchTerm.toLowerCase()) ||
      (user.email || '').toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      (user.role || '').toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  });

  result.sort((a, b) => {

    const nameA =
      `${a.firstName || ''} ${a.lastName || ''}`.toLowerCase();

    const nameB =
      `${b.firstName || ''} ${b.lastName || ''}`.toLowerCase();

    return this.sortOrder === 'asc'
      ? nameA.localeCompare(nameB)
      : nameB.localeCompare(nameA);
  });

  return result;
}

  // Pagination logic
  get paginatedUsers() {
    const startIndex = (this.CurrentPage - 1) * this.usersPerPage;
    return this.filteredAndSortedUsers.slice(startIndex, startIndex + this.usersPerPage);
  }

  get totalPages() {
    return Math.ceil(this.filteredAndSortedUsers.length / this.usersPerPage);
  }

  nextPage() {
    if (this.CurrentPage < this.totalPages) {
      this.CurrentPage++;
    }
  }

  previousPage() {
    if (this.CurrentPage > 1) {
      this.CurrentPage--;
    }
  }

  
}

