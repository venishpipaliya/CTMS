import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminServices } from '../../services/admin/admin.services';
import { Department } from '../../enum/department';
import { UserRole } from '../../enum/role';
@Component({
  selector: 'app-edit-users',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-users.component.html',
  styleUrl: './edit-users.component.scss'
})
export class EditUsersComponent implements OnInit {
  users: any[] = [];
  managers: any[] = [];
  
  departments = Object.values(Department);
  roles = Object.values(UserRole);
  
  editForm!: FormGroup;
  showModal = false;
  selectedUser: any = null;
  isSubmitted = false;
  constructor(
    private fb: FormBuilder,
    private adminService: AdminServices
  ) {}
  ngOnInit(): void {
    this.loadUsers();
    this.initForm();
    this.watchRoleChange();
  }
  // Load all users to display in the table
  loadUsers(): void {
    this.adminService.getAllUse().subscribe({
      next: (response: any) => {
        let userList: any[] = [];
        if (Array.isArray(response)) {
          userList = response;
        } else if (response && Array.isArray(response.data)) {
          userList = response.data;
        } else if (response && Array.isArray(response.result)) {
          userList = response.result;
        }
        this.users = userList;
      },
      error: (err) => {
        console.error('Failed to load users:', err);
      }
    });
  }
  // Lazy load managers from API and filter where role is 'Manager'
  private loadManagers(): void {
    this.adminService.getAllUse().subscribe({
      next: (response: any) => {
        let userList: any[] = [];
        if (Array.isArray(response)) {
          userList = response;
        } else if (response && Array.isArray(response.data)) {
          userList = response.data;
        } else if (response && Array.isArray(response.result)) {
          userList = response.result;
        }
        // Safe check for role matching (handles 'Manager' or 'manager' or 'MANAGER')
        this.managers = userList.filter(user => 
          user.role?.toLowerCase() === 'manager'
        );
      },
      error: (err) => {
        console.error('Failed to load managers from API:', err);
      }
    });
  }
  // Initialize the Edit reactive Form
  private initForm(): void {
    this.editForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      department: ['', Validators.required],
      role: ['', Validators.required],
      managerId: [{ value: '', disabled: true }],
      enabled: [true] // Active state toggle
    });
  }
  // Watch for role changes to enable/disable managerId input dynamically
  private watchRoleChange(): void {
    this.editForm.get('role')?.valueChanges.subscribe((selectedRole: string) => {
      const managerCtrl = this.editForm.get('managerId');
      
      if (selectedRole?.toUpperCase() === UserRole.EMPLOYEE) {
        managerCtrl?.enable();
        managerCtrl?.setValidators([Validators.required]);
        if (this.managers.length === 0) {
          this.loadManagers();
        }
      } else {
        managerCtrl?.disable();
        managerCtrl?.clearValidators();
        managerCtrl?.setValue('');
      }
      managerCtrl?.updateValueAndValidity();
    });
  }
  // Open popup modal and load user data
  openEditModal(user: any): void {
    this.selectedUser = user;
    this.isSubmitted = false;
    
    // Lazy load managers if editing an Employee and managers aren't loaded yet
    if (user.role?.toUpperCase() === UserRole.EMPLOYEE && this.managers.length === 0) {
      this.loadManagers();
    }
    // Set values into the form
    this.editForm.patchValue({
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      department: user.department,
      role: user.role,
      enabled: user.enabled
    });
    // Handle manager field enabling/validation based on role
    const managerCtrl = this.editForm.get('managerId');
    if (user.role?.toUpperCase() === UserRole.EMPLOYEE) {
      managerCtrl?.enable();
      managerCtrl?.setValidators([Validators.required]);
      managerCtrl?.setValue(user.managerId || '');
    } else {
      managerCtrl?.disable();
      managerCtrl?.clearValidators();
      managerCtrl?.setValue('');
    }
    managerCtrl?.updateValueAndValidity();
    this.showModal = true;
  }
  // Close the popup modal
  closeModal(): void {
    this.showModal = false;
    this.selectedUser = null;
    this.editForm.reset();
    this.isSubmitted = false;
  }
  // Update submission handler
  onSubmit(): void {
    this.isSubmitted = true;
    if (this.editForm.valid && this.selectedUser) {
      const formData = this.editForm.getRawValue();
      console.log('Updating user:', this.selectedUser.id, formData);
      this.adminService.updateUser(this.selectedUser.id, formData).subscribe({
        next: (response: any) => {
          alert(`User ${formData.firstName} ${formData.lastName} updated successfully!`);
          this.closeModal();
          this.loadUsers(); // Refresh list
        },
        error: (err: any) => {
          console.error('Error updating user:', err.error);
          alert(err.error?.message || err.error?.error || 'Failed to update user.');
        }
      });
    } else {
      console.log('Edit form is invalid');
    }
  }
  // Delete user by ID
  onDelete(userId: number, firstName: string, lastName: string): void {
    const confirmed = window.confirm(`Are you sure you want to delete user ${firstName} ${lastName}?`);
    
    if (confirmed) {
      this.adminService.deleteUser(userId).subscribe({
        next: (response: any) => {
          alert('User deleted successfully!');
          this.loadUsers(); // Refresh list
        },
        error: (err: any) => {
          console.error('Error deleting user:', err.error);
          alert(err.error?.message || err.error?.error || 'Failed to delete user.');
        }
      });
    }
  }
  // Helper method to check validation states
  isControlInvalid(controlName: string): boolean {
    const control = this.editForm.get(controlName);
    return !!(control && control.invalid && (control.touched || this.isSubmitted));
  }
}
