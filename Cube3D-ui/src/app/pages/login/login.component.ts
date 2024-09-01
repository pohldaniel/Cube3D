import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatIconRegistry, MatIconModule} from '@angular/material/icon';
import {DomSanitizer}from '@angular/platform-browser';
import {CommonModule} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {FormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatTooltipModule} from '@angular/material/tooltip';
import {AuthenticationService} from '../../services/authentication.service';
import {SnackService} from '../../services/snack.service';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {Person} from '../../models/Person';
import * as _ from 'lodash';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass', './login.component.css'],
  imports: [SpinnerComponent, CommonModule, FontAwesomeModule, FormsModule, MatCardModule, MatTooltipModule, MatIconModule],
  standalone: true
})
export class LoginComponent implements OnInit {

  returnUrl: string = '';
  username: string = 'admin';
  password: string = 'main100';
  show: boolean = false;

  isWait: boolean = false;
  dataLoaded: boolean = true;
  delayIntervall = 100;

  showLogout: boolean = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly authenticationService: AuthenticationService,
    private readonly snackService: SnackService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer){
      this.matIconRegistry.addSvgIcon("custom_questionmark", this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/svg/questmark.svg'));
  }

  ngOnInit(){   
    
    if (this.authenticationService.currentJwtValue) {  
         this.router.navigate(['/dashboard']);
    }
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';  
    this.dataLoaded = false;
    this.route.queryParams.subscribe({
      next: (params : any) => {
        if(!_.isEmpty(params)) {
          this.authenticationService.gethTokenOIDC(params['code']).subscribe( {
            next: (response : any) =>{
              this.authenticationService.setTokenMapValue(response);
              this.authenticationService.setCurrentJwtValue(response.token);        
              this.authenticationService.setLoginValue("oidc");  
              //this.dataLoaded = true;
              let path : string = this.returnUrl === '/' ? '/dashboard': this.returnUrl;
              this.router.navigate([path]);   
            },
            error: (error : HttpErrorResponse)=> { 
              //this.dataLoaded = true;               
              this.router.navigate(['/errorpage']);              
            }
          })
        }else{
          this.dataLoaded = true;
        }  
      },
      error: (error : HttpErrorResponse)=> {     
        this.dataLoaded = true;         
        this.router.navigate(['/errorpage']);            
      }
    }); 
  }

  onLogin() {
    if(this.username){
      this.authenticationService.login(this.username, this.password)
      .subscribe({
        next: (person : Person) => {

          this.authenticationService.getToken(person).subscribe({
            next: (data : any) => {
              this.authenticationService.setTokenMapValue(data);
              this.authenticationService.setCurrentJwtValue(data.token);
              this.authenticationService.setLoginValue("jwt");
              this.dataLoaded = true;
              let path : string = this.returnUrl === '/' ? '/dashboard': this.returnUrl;
              this.router.navigate([path]);    
            },
            error: (error : HttpErrorResponse) => {
              
              this.router.navigate(['/errorpage']);
            }
          })

          
        },
        error: (error : HttpErrorResponse) => {
          this.router.navigate(['/errorpage']);
        }
      })
    }
  }

  toggle() {
    this.show = !this.show;
  }
}
