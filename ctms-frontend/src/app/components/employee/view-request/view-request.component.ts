import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { EmployeeService } from '../../services/employee/employee.service';

// Custom validator to ensure Start Date <= End Date
export function dateRangeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const start = control.get('startDate')?.value;
    const end = control.get('endDate')?.value;
    
    if (start && end) {
      const startDate = new Date(start);
      const endDate = new Date(end);
      if (startDate > endDate) {
        return { dateRangeInvalid: true };
      }
    }
    return null;
  };
}

@Component({
  selector: 'app-view-request',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './view-request.component.html',
  styleUrl: './view-request.component.scss'
})
export class ViewRequestComponent implements OnInit {

  requests: any[] = [];
  filteredRequests: any[] = [];

  selectedRequest: any = null;

  showEditModal = false;
  showDetailsModal = false;

  searchQuery = '';
  selectedStatusFilter = 'ALL';

  editForm!: FormGroup;

  isDateRangeInvalid = false;

  // change employee id after login integration
  employeeId = 1;

  stats = {
    total: 0,
    draft: 0,
    pending: 0,
    approved: 0
  };

  toast = {
    show: false,
    message: '',
    type: 'success'
  };

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadRequests();
  }

  // =========================
  // FORM
  // =========================

  initForm() {
    this.editForm = this.fb.group({
      destination: ['', [Validators.required]],
      purpose: ['', [Validators.required]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      estimatedCost: ['', Validators.required],
      travelClass: ['Economy', Validators.required]
    });

    this.editForm.valueChanges.subscribe(() => {
      this.validateDateRange();
    });
  }

  validateDateRange() {
    const start = this.editForm.get('startDate')?.value;
    const end = this.editForm.get('endDate')?.value;

    if (start && end) {
      this.isDateRangeInvalid = new Date(start) > new Date(end);
    } else {
      this.isDateRangeInvalid = false;
    }
  }

  // =========================
  // LOAD DATA
  // =========================

  loadRequests() {
    this.employeeId = 10;
    this.employeeService
    .getAllRequests(this.employeeId)
    .subscribe({
      next: (response: any) => {

        // if backend returns { data: [...] }
        this.requests = response.data || [];

        this.filteredRequests = [...this.requests];

        this.updateStats();
      },
      error: (err) => {
        console.error(err);
        this.showToast('Failed to load requests', 'danger');
      }
    });
  }

  updateStats() {
    this.stats.total = this.requests.length;

    this.stats.draft = this.requests.filter(
      r => r.status?.toUpperCase() === 'DRAFT'
    ).length;

    this.stats.pending = this.requests.filter(
      r =>
        r.status?.toUpperCase() === 'SUBMITTED' ||
        r.status?.toUpperCase() === 'MANAGER_APPROVED'
    ).length;

    this.stats.approved = this.requests.filter(
      r =>
        r.status?.toUpperCase() === 'FINANCE_APPROVED' ||
        r.status?.toUpperCase() === 'COMPLETED'
    ).length;
  }

  // =========================
  // SEARCH
  // =========================

  searchRequests(value: string) {
    this.searchQuery = value;
    this.applyFilters();
  }

  filterRequests(status: string) {
    this.selectedStatusFilter = status;
    this.applyFilters();
  }

  applyFilters() {
  this.filteredRequests = this.requests.filter(req => {

    const matchesSearch =
      req.destination
        ?.toLowerCase()
        .includes(this.searchQuery.toLowerCase()) ||

      req.purpose
        ?.toLowerCase()
        .includes(this.searchQuery.toLowerCase());

    let matchesStatus = false;

    if (this.selectedStatusFilter === 'ALL') {
      matchesStatus = true;

    } else if (this.selectedStatusFilter === 'REJECTED') {
      matchesStatus =
        req.status === 'MANAGER_REJECTED' ||
        req.status === 'FINANCE_REJECTED';

    } else {
      matchesStatus =
        req.status?.toUpperCase() ===
        this.selectedStatusFilter;
    }

    return matchesSearch && matchesStatus;
  });
}

  // =========================
  // CREATE NEW
  // =========================

  createNewDraft() {
    this.selectedRequest = null;

    this.editForm.reset({
      destination: '',
      purpose: '',
      startDate: '',
      endDate: '',
      estimatedCost: '',
      travelClass: 'Economy'
    });

    this.showEditModal = true;
  }

  // =========================
  // EDIT
  // =========================

  openEditModal(req: any) {
    this.selectedRequest = req;

    this.editForm.patchValue({
      destination: req.destination,
      purpose: req.purpose,
      startDate: req.startDate,
      endDate: req.endDate,
      estimatedCost: req.estimatedCost,
      travelClass: req.travelClass
    });

    this.showEditModal = true;
  }

  closeEditModal() {
    this.showEditModal = false;
  }

  saveEdit() {
    if (this.editForm.invalid || this.isDateRangeInvalid) {
      return;
    }

    const data = this.editForm.value;

    // update
    if (this.selectedRequest) {
      this.employeeService
        .updateRequest(this.selectedRequest.id, data)
        .subscribe({
          next: () => {
            this.closeEditModal();
            this.loadRequests();
            this.showToast('Request updated successfully', 'success');
          },
          error: () => {
            this.showToast('Update failed', 'danger');
          }
        });
    }

    // create new draft
    else {
      this.employeeService
        .saveDraft(this.employeeId, data)
        .subscribe({
          next: () => {
            this.closeEditModal();
            this.loadRequests();
            this.showToast('Draft saved successfully', 'success');
          },
          error: () => {
            this.showToast('Save failed', 'danger');
          }
        });
    }
  }

  // =========================
  // SUBMIT
  // =========================

  submitRequest(id: number) {
    this.employeeService
      .submitRequest(id, {})
      .subscribe({
        next: () => {
          this.loadRequests();
          this.showToast('Request submitted', 'success');
        },
        error: () => {
          this.showToast('Submit failed', 'danger');
        }
      });
  }

  // =========================
  // CANCEL
  // =========================

  cancelRequest(id: number) {
    this.employeeService
      .cancelRequest(id)
      .subscribe({
        next: () => {
          this.loadRequests();
          this.showToast('Request cancelled', 'warning');
        },
        error: () => {
          this.showToast('Cancel failed', 'danger');
        }
      });
  }

  // =========================
  // DETAILS
  // =========================

  openDetailsModal(req: any) {
    this.selectedRequest = req;
    this.showDetailsModal = true;
  }

  closeDetailsModal() {
    this.showDetailsModal = false;
  }

  // =========================
  // TOAST
  // =========================

  showToast(message: string, type: any) {
    this.toast.message = message;
    this.toast.type = type;
    this.toast.show = true;

    setTimeout(() => {
      this.hideToast();
    }, 3000);
  }

  hideToast() {
    this.toast.show = false;
  }
}