import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminServices } from '../../services/admin/admin.services'; // Inject the admin services
import { Department } from '../../enum/department';
import { UserRole } from '../../enum/role';


@Component({
  selector: 'app-add-users',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-users.component.html',
  styleUrl: './add-users.component.scss'
})
export class AddUsersComponent implements OnInit {
  userForm!: FormGroup;
  
  // Convert enums to array for easy loop rendering in HTML
  departments = Object.values(Department);
  roles = Object.values(UserRole);
  
  // Stores list of managers fetched from API
  managers: any[] = [];

  // Track form submission attempt
  isSubmitted = false;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminServices // Injected admin service
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.watchRoleChange(); // We no longer call loadManagers() on init (Lazy Loading)
  }

  // Fetch users from API and filter where role is 'Manager'
  private loadManagers(): void {
    this.adminService.getAllUse().subscribe({
      next: (response: any) => {
        // Defensive parsing: Extract the array regardless of the API response wrapper shape
        let userList: any[] = [];
        if (Array.isArray(response)) {
          userList = response;
        } else if (response && Array.isArray(response.data)) {
          userList = response.data;
        } else if (response && Array.isArray(response.result)) {
          userList = response.result;
        }

        // Safe check for role matching (handles 'Manager' or 'manager')
        this.managers = userList.filter(user => 
          user.role?.toLowerCase() === 'manager'
        );
        console.log('Managers loaded successfully:', this.managers);
      },
      error: (err) => {
        console.error('Failed to load managers from API:', err);
      }
    });
  }

  // Initialize the Reactive Form with proper validation rules
  private initForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      department: ['', Validators.required],
      role: ['', Validators.required],
      // Store the unique ID of the selected manager
      managerId: [{ value: '', disabled: true }],
      password: ['Password123', [Validators.required, Validators.minLength(8)]]
    });
  }

  // Subscribe to changes in the Role dropdown to enable/disable the Manager control
  private watchRoleChange(): void {
    this.userForm.get('role')?.valueChanges.subscribe((selectedRole: string) => {
      const managerCtrl = this.userForm.get('managerId');
      
      if (selectedRole === UserRole.EMPLOYEE) {
        // If the role is Employee, enable manager control and make it required
        managerCtrl?.enable();
        managerCtrl?.setValidators([Validators.required]);

        // LAZY LOADING: Fetch from API only when Employee is selected and managers are not loaded yet
        if (this.managers.length === 0) {
          this.loadManagers();
        }
      } else {
        // If the role is Admin or Manager, disable the control and clear validation/value
        managerCtrl?.disable();
        managerCtrl?.clearValidators();
        managerCtrl?.setValue('');
      }
      
      // Update form state to apply new validation rules
      managerCtrl?.updateValueAndValidity();
    });
  }

  // Form submission handler
  onSubmit(): void {
  this.isSubmitted = true;

  if (this.userForm.valid) {

    // Get all values from form
    const formData = this.userForm.getRawValue();

    console.log('Sending data:', formData);

    // Call backend API
    this.adminService.saveNewUser(formData).subscribe({
      next: (response: any) => {
        console.log('User saved successfully:', response);

        alert(
          `User ${formData.firstName} ${formData.lastName} added successfully!`
        );

        // Reset form
        this.userForm.reset();
        this.isSubmitted = false;

        // Disable manager again after reset
        this.userForm.get('managerId')?.disable();
      },

      error: (err: any) => {
        console.error('Error saving user:', err.error);

        alert(err.error?.error);
      }
    });

  } else {
    console.log('Form is invalid');
  }
}

  // Helper method to easily check validation states in HTML
  isControlInvalid(controlName: string): boolean {
    const control = this.userForm.get(controlName);
    return !!(control && control.invalid && (control.touched || this.isSubmitted));
  }

  
}
