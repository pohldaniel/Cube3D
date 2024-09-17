import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Person} from '../models/Person';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Role} from '../models/Role.enum';

@Injectable({providedIn: 'root'}) export class AuthenticationService {

  public routeSubject: BehaviorSubject<string | null>;
  public route: Observable<string | null>;

  public jwtSubject: BehaviorSubject<string | null>;
  public jwt: Observable<string | null>;

  public tokenMapSubject: BehaviorSubject<any>;
  public tokenMap: Observable<any>;

  public providerSubject: BehaviorSubject<string | null>;
  public provider: Observable<string | null>;

  headers = new HttpHeaders().set('Content-Type', 'application/json');

  constructor(private readonly http: HttpClient, private readonly jwtService : JwtHelperService) {

    this.routeSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('route'));
    this.route = this.routeSubject.asObservable();

    this.jwtSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('jwt'));
    this.jwt = this.jwtSubject.asObservable();

    this.tokenMapSubject = new BehaviorSubject<any>(sessionStorage.getItem('tokenMap'));
    this.tokenMap = this.tokenMapSubject.asObservable();

    this.providerSubject = new BehaviorSubject<any>(sessionStorage.getItem('provider'));
    this.provider = this.providerSubject.asObservable();
  }

  public get routeValue(): string {
    return this.routeSubject.value as string;
  }

  public get jwtValue(): string {
    return this.jwtSubject.value as string;
  }

  public get tokenMapValue(): any {
    return this.tokenMapSubject.value;
  }

  public get providerValue(): string {
    return this.providerSubject.value as string;
  }

  public setCurrentRouteValue(route: string) {
    sessionStorage.setItem('route', route);
    this.routeSubject.next(route);
  }

  public setCurrentJwtValue(jwt: string) {
    sessionStorage.setItem('jwt', jwt);
    this.jwtSubject.next(jwt);
  }

  public setTokenMapValue(tokenMap: any) {
    sessionStorage.setItem('tokenMap', JSON.stringify(tokenMap));
    this.tokenMapSubject.next(JSON.stringify(tokenMap));
  }

  public setProvider(provider: string) {
    sessionStorage.setItem('provider', provider);
    this.providerSubject.next(provider);
  }

  login(id: string, password: string) {
    return this.http.post<Person>(environment.baseUrl + '/auth/authenticate', {id, password}, {headers: this.headers});
  }

  getToken(person: Person) {
    return this.http.post<Person>(environment.baseUrl + '/auth/token', JSON.stringify(person), {headers: this.headers});
  }

  refreshToken() : Observable<string> {
    return this.http.post<string>(environment.baseUrl + '/auth/refresh', {'token': this.jwtSubject.value}, {headers: this.headers});   
  }

  gethTokenOIDC(code : string){
    const params = new HttpParams()
    .set('code', code)
    return this.http.get<any[]>(environment.baseUrl + '/' + this.providerSubject.value as String + '/oidc/token', {params, headers: this.headers});
  }

  refreshTokenOIDC() : Observable<string> {
    return this.http.post<string>(environment.baseUrl + '/' + this.providerSubject.value as String  + '/oidc/refresh', {'token': JSON.parse(this.tokenMapSubject.value as string).token, 'accessToken': JSON.parse(this.tokenMapSubject.value as string).accessToken, 'user': JSON.parse(this.tokenMapSubject.value as string).user, 'refreshToken': JSON.parse(this.tokenMapSubject.value as string).refreshToken}, {headers: this.headers});   
  }

  logout() {
    sessionStorage.removeItem('route');
    this.routeSubject.next(null);

    sessionStorage.removeItem('jwt');
    this.jwtSubject.next(null);

    sessionStorage.removeItem('tokenMap');
    this.tokenMapSubject.next(null);

    sessionStorage.removeItem('provider');
    this.providerSubject.next(null);

  }

  public getCurrentRoles() : Role[] {
    return this.jwtService.decodeToken(this.jwtValue).roles;
  }

  public getCurrentSub() : string{
    return this.jwtService.decodeToken(this.jwtValue).sub;
  }

  public getCurrentUser() : string{
    return JSON.parse(this.tokenMapSubject.value as string).user;
  }

  public getCurrentProvider() : string{
    return this.providerSubject.value as string;
  }

  public containsRole(roles: string[]) : boolean{
    return this.getCurrentRoles().some(e => roles.some(role => e === role));
  }
}
