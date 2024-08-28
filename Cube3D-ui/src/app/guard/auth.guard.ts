import {inject} from '@angular/core';
import {CanActivateFn, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../services/authentication.service';
import {SnackService} from '../services/snack.service';
import {Role} from '../models/Role.enum';
import {of, tap, map, catchError} from 'rxjs';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> => {

  const router = inject(Router);
  const authenticationService = inject(AuthenticationService);
  const currentJwt = authenticationService.currentJwtValue;
  const snackService = inject(SnackService);
  let found : boolean = false;

  if (currentJwt) {
    return authenticationService.refreshToken().pipe(
      tap((data: any) => {
        if(data.token){
          authenticationService.setCurrentJwtValue(data.token);
          found = route.data['Roles'].some((role : Role) => authenticationService.getCurrentRoles().some(e => e === role));
          if(found){
            authenticationService.setCurrentRouteValue(state.url);
            return found;
          }else{
            router.navigate([authenticationService.currentRouteValue]);
            return found;
          } 
        }else{     
          snackService.open('ERROR', "Token Expired");
          authenticationService.logout();
          router.navigate(['/login']);
          return false;
        }
      }),
      map(() => true),
      catchError(() => {
        authenticationService.logout();
        router.navigate(['route-to-fallback-page']);
        return of(false);
      })
    );
  }else{
    router.navigate(['/login'], { queryParams: { returnUrl: state.url.replace(/\//g, "") }});
    return false;
  }
};