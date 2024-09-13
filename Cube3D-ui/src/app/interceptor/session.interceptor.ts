import {inject} from '@angular/core';
import {HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse,} from '@angular/common/http';
import {AuthenticationService} from '../services/authentication.service';

export const intercept: HttpInterceptorFn = (request : HttpRequest<unknown>, next : HttpHandlerFn) => {
  const authenticationService = inject(AuthenticationService);

  if(request.url.indexOf('/restAPI') > -1 && authenticationService.jwtValue){      
    request = request.clone({headers: request.headers.set('Authorization', 'Bearer ' + authenticationService.jwtValue)});    
  }
  return next(request);
};