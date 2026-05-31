import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FinanceService } from '../../services/finance/finance.service';

@Component({
  selector: 'app-finance-approved',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './finance-approved.component.html',
  styleUrl: './finance-approved.component.scss'
})
export class FinanceApprovedComponent implements OnInit {

  financeId: number = 4; 
  
    // Component State
    requests: any[] = [];
    filteredRequests: any[] = [];
    searchQuery: string = '';
    selectedStatusFilter: string = 'ALL';
  
    // Statistics
    stats = {
      total: 0,
      pending: 0,
      approved: 0,
      rejected: 0
    };
  
    // Modals and selections
    selectedRequest: any = null;
    showActionModal: boolean = false;
    showDetailsModal: boolean = false;
    actionType: 'APPROVE' | 'REJECT' = 'APPROVE';
  
    // Form Groups
    actionForm!: FormGroup;
  
    // Feedback notifications (Toasts)
    toast = {
      show: false,
      type: 'success', // success, info, warning, danger
      message: ''
    };
  
    private toastTimeout: any;
  
   
  
    constructor(
      private fb: FormBuilder,
      private financeService: FinanceService
    ) { }
  
    ngOnInit(): void {
      this.initActionForm();
      this.loadRequests();
    }
  
    // Initialize Action Form with comment field validated as mandatory
    private initActionForm(): void {
      this.actionForm = this.fb.group({
        comment: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(500)]]
      });
    }
  
    // Fetch submitted requests for finance 4 from API
    loadRequests(): void {
      this.financeService.getAllRequests().subscribe({
        next: (response: any) => {
  
          if (response && response.data?.length > 0) {
            this.requests = response.data;
  
            this.showToast(
              'success',
              'Load successful.'
            );
  
          } else {
            // API returned empty list
            this.requests = [];
  
            this.showToast(
              'info',
              'No submitted requests found.'
            );
          }
  
          this.postLoadSetup();
        },
  
        error: (err) => {
          console.error('Error loading submitted requests:', err);
  
          this.requests = [];
  
          this.showToast(
            'error',
            'Failed to load submitted requests from server.'
          );
  
          this.postLoadSetup();
        }
      });
    }
  
  
  
    // Set filters and stats
    private postLoadSetup(): void {
      this.calculateStats();
      this.applyFiltersAndSearch();
    }
  
    // Calculate statistics metrics
    calculateStats(): void {
      let total = this.requests.length;
      let pending = 0;
      let approved = 0;
      let rejected = 0;
  
      this.requests.forEach(req => {
        const status = req.status ? req.status.toUpperCase() : '';
        if (status === 'SUBMITTED') {
          pending++;
        } else if (status === 'MANAGER_APPROVED' || status === 'FINANCE_APPROVED' || status === 'COMPLETED') {
          approved++;
        } else if (status === 'FINANCE_REJECTED') {
          rejected++;
        }
      });
  
      this.stats = { total, pending, approved, rejected };
    }
  
    // Filter requests locally
    filterRequests(status: string): void {
      this.selectedStatusFilter = status;
      this.applyFiltersAndSearch();
    }
  
    // Search requests by Employee Name, Destination, or Purpose locally
    searchRequests(query: string): void {
      this.searchQuery = query;
      this.applyFiltersAndSearch();
    }
  
    // Apply filters and search
    private applyFiltersAndSearch(): void {
      let temp = [...this.requests];
  
      // 1. Status Filter Tab
      if (this.selectedStatusFilter !== 'ALL') {
        const filter = this.selectedStatusFilter.toUpperCase();
        temp = temp.filter(req => {
          const reqStatus = req.status ? req.status.toUpperCase() : '';
          if (filter === 'PENDING') {
            return reqStatus === 'SUBMITTED';
          } else if (filter === 'APPROVED') {
            return reqStatus === 'MANAGER_APPROVED' || reqStatus === 'FINANCE_APPROVED' || reqStatus === 'COMPLETED';
          } else if (filter === 'REJECTED') {
            return reqStatus === 'FINANCE_REJECTED';
          }
          return true;
        });
      }
  
      // 2. Search Query (Employee Name, Destination, Purpose)
      if (this.searchQuery && this.searchQuery.trim() !== '') {
        const query = this.searchQuery.toLowerCase().trim();
        temp = temp.filter(req => 
          (req.employeeName && req.employeeName.toLowerCase().includes(query)) ||
          (req.destination && req.destination.toLowerCase().includes(query)) ||
          (req.purpose && req.purpose.toLowerCase().includes(query))
        );
      }
  
      // Sort by id descending so newest are on top
      this.filteredRequests = temp.sort((a, b) => b.id - a.id);
    }
  
    // Open Action modal with details
    openActionModal(request: any, type: 'APPROVE' | 'REJECT'): void {
      this.selectedRequest = request;
      this.actionType = type;
      this.actionForm.reset({ comment: '' });
      this.showActionModal = true;
    }
  
    // Close Action modal
    closeActionModal(): void {
      this.showActionModal = false;
      this.selectedRequest = null;
      this.actionForm.reset();
    }
  
    // Open View Details Modal
    openDetailsModal(request: any): void {
      this.selectedRequest = request;
      this.showDetailsModal = true;
    }
  
    // Close Details Modal
    closeDetailsModal(): void {
      this.showDetailsModal = false;
      this.selectedRequest = null;
    }
  
    // Submit approval/rejection action
    submitAction(): void {
      if (this.actionForm.invalid) {
        this.actionForm.markAllAsTouched();
        return;
      }
  
      const commentValue = this.actionForm.value.comment;
      const reqId = this.selectedRequest.id;
      const obj = { comment: commentValue };
  
      if (this.actionType === 'APPROVE') {
        this.financeService.approveRequest(reqId, obj).subscribe({
          next: (res) => {
            this.updateLocalStatusAndComments(
              reqId,
              'FINANCE_APPROVED',
              commentValue,
            );
  
            this.closeActionModal();
  
            this.showToast('success', 'Approved successfully');
          },
  
          error: (err) => {
            console.error('Approve failed:', err);
  
            this.showToast('danger', 'Error occurred while approving request');
          },
        });
      }
  
      if (this.actionType === 'REJECT') {
        this.financeService.rejectRequest(reqId, obj).subscribe({
          next: (res) => {
            this.updateLocalStatusAndComments(
              reqId,
              'FINANCE_REJECTED',
              commentValue,
            );
  
            this.closeActionModal();
  
            this.showToast('success', 'Rejected successfully');
          },
  
          error: (err) => {
            console.error('Reject failed:', err);
  
            this.showToast('danger', 'Error occurred while rejecting request');
          },
        });
      }
    }
  
    // Update request properties locally and refresh views
    private updateLocalStatusAndComments(id: number, status: string, comments: string): void {
      const idx = this.requests.findIndex(r => r.id === id);
      if (idx !== -1) {
        this.requests[idx] = { 
          ...this.requests[idx], 
          status: status, 
          financeComments: comments,
          financeActionAt: new Date().toISOString()
        };
        this.postLoadSetup();
      }
    }
  
    // Show customized Toast notifications
    showToast(type: string, message: string): void {
      if (this.toastTimeout) {
        clearTimeout(this.toastTimeout);
      }
      
      this.toast = {
        show: true,
        type,
        message
      };
  
      this.toastTimeout = setTimeout(() => {
        this.toast.show = false;
      }, 5000);
    }
  
    // Hide toast manually
    hideToast(): void {
      this.toast.show = false;
    }

}
