import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AdminServices {

  apiUri: string = "http://localhost:8080/api/users/";

  constructor(private http: HttpClient) {  }

  getAllUse() {
      return this.http.get<any[]>(this.apiUri + "getAllUsers");
    }

  saveNewUser(obj:any) {

    return this.http.post(this.apiUri + "saveNewUser", obj)
  }
}
