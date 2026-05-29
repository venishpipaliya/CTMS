import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { EmployeeService } from '../../services/employee/employee.service';

@Component({
  selector: 'app-new-request',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './new-request.component.html',
  styleUrl: './new-request.component.scss',
})
export class NewRequestComponent implements OnInit {
  travelForm!: FormGroup;
  isSubmitted = false;
  showSuccessToast = false;
  savedData: any = null;
  constructor(
    private fb: FormBuilder, 
    private employeeService: EmployeeService
  ) {}
  ngOnInit(): void {
    this.initForm();
  }
  private initForm(): void {
    this.travelForm = this.fb.group(
      {
        destination: ['', [Validators.required, Validators.minLength(3)]],
        travelClass: ['', Validators.required],
        startDate: ['', [Validators.required]],
        endDate: ['', [Validators.required]],
        purpose: ['', [Validators.required, Validators.maxLength(500)]],
        estimatedCost: ['', [Validators.required, Validators.min(1)]],
      },
      { validators: this.dateLessThan('startDate', 'endDate') },
    );
  }
  // Custom Validator to verify start date is before or equal to end date
  private dateLessThan(from: string, to: string) {
    return (group: AbstractControl): ValidationErrors | null => {
      const start = group.get(from);
      const end = group.get(to);
      if (start && end && start.value && end.value) {
        const startDate = new Date(start.value);
        const endDate = new Date(end.value);
        if (endDate < startDate) {
          end.setErrors({ dateLessThan: true });
          return { dateLessThan: true };
        } else {
          // If no other errors exist on end control, clear it
          if (end.hasError('dateLessThan')) {
            const errs = end.errors;
            if (errs) {
              delete errs['dateLessThan'];
              end.setErrors(Object.keys(errs).length ? errs : null);
            }
          }
        }
      }
      return null;
    };
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.travelForm.valid) {
      this.savedData = this.travelForm.value;
      this.showSuccessToast = true;


      const employeeId = 10; // Replace with actual employee ID from auth context

      const requestData = this.travelForm.value;

      this.employeeService.saveDraft(employeeId, requestData).subscribe({
        next: (response: any) => {
          console.log('Draft saved successfully:', response);
        },
        error: (error: any) => {
          console.error('Error saving draft:', error);
        }
      });

      // Auto-hide success overlay after 4 seconds
      setTimeout(() => {
        this.showSuccessToast = false;
      }, 4000);
    } else {
      // Mark all controls as touched to show errors
      this.travelForm.markAllAsTouched();
    }
  }
  resetForm(): void {
    this.travelForm.reset();
    this.isSubmitted = false;
    this.showSuccessToast = false;
    this.savedData = null;
  }
}
