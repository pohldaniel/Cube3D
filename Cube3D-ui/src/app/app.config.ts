import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import {provideRouter} from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {IMAGE_CONFIG} from '@angular/common';
import {provideHttpClient, withInterceptors, HTTP_INTERCEPTORS} from '@angular/common/http';
import {routes} from './app.routes';
import {intercept} from './interceptor/session.interceptor';
import {JWT_OPTIONS, JwtHelperService} from '@auth0/angular-jwt';

export const appConfig: ApplicationConfig = {
  providers: [
     provideRouter(routes),
     importProvidersFrom(BrowserAnimationsModule),
     provideHttpClient(withInterceptors([intercept])),
     {
      provide: IMAGE_CONFIG,
      useValue: {
        disableImageSizeWarning: true
      },      
    },
    /*{
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    }*/
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService
  ]
};
