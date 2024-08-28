import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Timesheet} from '../models/Timesheet';
import {Person} from '../models/Person';

@Injectable({
    providedIn: 'root'
})
export class DatabaseService {
  
  headers = new HttpHeaders().set('Content-Type', 'application/json');
  
  constructor(private readonly http: HttpClient) { }
 
  getTimesheets(): Observable<Timesheet[]> {    
    return  this.http.get<Timesheet[]>(environment.apiUrl + '/ltservice/getAll').pipe(
      map((response: Timesheet[]) => {   
        return response.map(x => new Timesheet(x) );
      })
    );
  } 

  getPersons(): Observable<Person[]> {  
    return  this.http.get<Person[]>(environment.apiUrl + '/persons/getAll').pipe(
      map((response: Person[]) => {   
        return response.map(x => new Person(x) );
      })
    );
  }

  createPerson(person: Person): Observable<Person> {
    return this.http.post<Person>(
      environment.apiUrl + '/persons/create',
      JSON.stringify(person),
      {headers: this.headers});
  }

  updatePerson(person: Person): Observable<string> {
    return this.http.post<string>(
      environment.apiUrl + '/persons/update',
      JSON.stringify(person),
      {headers: this.headers});
  }

  deletePerson(person: Person): Observable<string> {
    return this.http.post<string>(
      environment.apiUrl + '/persons/delete',
      JSON.stringify(person),
      {headers: this.headers});
  }

  generateLink(person: Person): Observable<string> {
    return this.http.post<string>(
      environment.apiUrl + '/persons/generate',
      JSON.stringify(person),
      {headers: this.headers});
  }

  removeLink(person: Person): Observable<string> {
    return this.http.post<string>(
      environment.apiUrl + '/persons/remove',
      JSON.stringify(person),
      {headers: this.headers});
  }

  logLevel(){
    return this.http.get<any>(environment.apiUrl + '/info/getLogLevel');
  }

  setLogLevel(logLevel: string){
    return this.http.post<any>(
      environment.apiUrl + '/info/setLogLevel',
      {'logLevel': logLevel},
      {headers: this.headers});
  }
}