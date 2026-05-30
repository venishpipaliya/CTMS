import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
apiUri: string = "http://localhost:8080/api/request/";

  constructor(private http: HttpClient) { }


  getEmployeeRequests(id: number) {
    return this.http.get<any[]>(this.apiUri + "getSubmittedRequestsByManager/" + id);
  }

  approveRequest(requestId: number, managerId: number, obj: any) {
    return this.http.post(this.apiUri + requestId + "/manager/" + managerId + "/approve", obj);
  }

  rejectRequest(requestId: number, managerId: number, obj: any) {
    return this.http.post(this.apiUri + requestId + "/manager/" + managerId + "/reject", obj);
  }

  saveDraft(id: number, obj: any) {
    return this.http.post(this.apiUri + id + "/draft", obj);
  }

  submitRequest(id: number, obj: any) {
    return this.http.put(this.apiUri + id + "/submit", obj);
  }

  cancelRequest(id: number) {
    return this.http.put(this.apiUri + id + "/cancle", null);
  }

  updateRequest(id: number, obj: any) {
    return this.http.put(this.apiUri + id + "/update", obj);
  }

  getAllRequests(id: number) {
    return this.http.get<any[]>(this.apiUri + "getEmployeeRequests/" + id);
  } 


}
