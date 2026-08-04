import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { FootballField } from '../models/football-field';


@Injectable({
  providedIn: 'root'
})
export class FootballFieldService {


  private apiUrl = 'http://localhost:8080/api/fields';


  constructor(
    private http: HttpClient
  ) { }



  getAll(): Observable<FootballField[]> {

    return this.http.get<FootballField[]>(
      this.apiUrl
    );

  }

}