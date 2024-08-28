import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Person} from '../models/Person';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Role} from '../models/Role.enum';

@Injectable({providedIn: 'root'}) export class AuthenticationService {

  public currentRouteSubject: BehaviorSubject<string | null>;
  public currentRoute: Observable<string | null>;

  public currentJwtSubject: BehaviorSubject<string | null>;
  public currentJwt: Observable<string | null>;

  headers = new HttpHeaders().set('Content-Type', 'application/json');

  constructor(private readonly http: HttpClient, private readonly jwtService : JwtHelperService) {

    this.currentRouteSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('currentRoute'));
    this.currentRoute = this.currentRouteSubject.asObservable();

    this.currentJwtSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('currentJwt'));
    this.currentJwt = this.currentJwtSubject.asObservable();
  }

  public get currentRouteValue(): string {
    return this.currentRouteSubject.value as string;
  }

  public get currentJwtValue(): string {
    return this.currentJwtSubject.value as string;
  }

  public setCurrentRouteValue(route: string) {
    sessionStorage.setItem('currentRoute', route);
    this.currentRouteSubject.next(route);
  }

  public setCurrentJwtValue(route: string) {
    sessionStorage.setItem('currentJwt', route);
    this.currentJwtSubject.next(route);
  }

  login(id: string, passwordHash: string) {
    return this.http.post<Person>(environment.baseUrl + '/auth/authenticate', {id, passwordHash}, {headers: this.headers});
  }

  token(person: Person) {
    return this.http.post<Person>(environment.baseUrl + '/auth/token', JSON.stringify(person), {headers: this.headers});
  }

  refreshToken() : Observable<string> {
    return this.http.post<string>(environment.baseUrl + '/auth/refresh', {'token': this.currentJwtSubject.value}, {headers: this.headers});   
  }

  logout() {
    sessionStorage.removeItem('currentRoute');
    this.currentRouteSubject.next(null);

    sessionStorage.removeItem('currentJwt');
    this.currentJwtSubject.next(null);
  }

  public getCurrentRoles() : Role[] {
    return this.jwtService.decodeToken(this.currentJwtValue).roles;
  }

  public getCurrentId() : string{
    return this.jwtService.decodeToken(this.currentJwtValue).sub;
  }

  public containsRole(roles: string[]) : boolean{
    return this.getCurrentRoles().some(e => roles.some(role => e === role));
  }
}
