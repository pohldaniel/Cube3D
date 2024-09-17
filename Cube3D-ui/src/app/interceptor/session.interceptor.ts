import {inject} from '@angular/core';
import {HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse,} from '@angular/common/http';
import {AuthenticationService} from '../services/authentication.service';

export const intercept: HttpInterceptorFn = (request : HttpRequest<unknown>, next : HttpHandlerFn) => {
  const authenticationService = inject(AuthenticationService);

  if(request.url.indexOf('/restAPI') > -1 && authenticationService.jwtValue && authenticationService.tokenMapValue){      
    request = request.clone({headers: request.headers.set('Authorization', 'Bearer ' + JSON.parse(authenticationService.tokenMapSubject.value as string).token)
                                                     .set('Provider', authenticationService.providerValue)
    });    
  }
  return next(request);
};