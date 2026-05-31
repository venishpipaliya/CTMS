import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FinanceService {

  apiUri: string = "http://localhost:8080/api/request/";

  constructor(private http: HttpClient) { }


  approveRequest(requestId: number,  obj: any) {
    return this.http.post(this.apiUri + requestId + "/finance/"  + "approve", obj);
  }

  rejectRequest(requestId: number,  obj: any) {
    return this.http.post(this.apiUri + requestId + "/finance/"  + "reject", obj);
  }

  

  getAllRequests() {
    return this.http.get<any[]>(this.apiUri + "getFinanceRequests" );
  } 
}
