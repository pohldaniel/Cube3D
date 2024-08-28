import {inject} from '@angular/core';
import {HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse,} from '@angular/common/http';
import {AuthenticationService} from '../services/authentication.service';
import {tap} from 'rxjs/operators';


export const intercept: HttpInterceptorFn = (request : HttpRequest<unknown>, next : HttpHandlerFn) => {
  const authenticationService = inject(AuthenticationService);

  if(request.url.indexOf('/restAPI') > -1 && authenticationService.currentJwtValue){        
    request = request.clone({headers: request.headers.set('Authorization', 'Bearer ' + authenticationService.currentJwtValue)});    
  }
  return next(request);
};