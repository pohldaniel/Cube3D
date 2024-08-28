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

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass', './login.component.css'],
  imports: [SpinnerComponent, CommonModule, FontAwesomeModule, FormsModule, MatCardModule, MatTooltipModule, MatIconModule],
  standalone: true
})
export class LoginComponent implements OnInit {

  showRootPWForm = true;
  returnUrl: string = '';
  //username: string = 'actionmanager'; 
  username: string = 'admin';
  password: string = 'main100';
  show: boolean = false;

  isWait: boolean = false;
  dataLoaded: boolean = true;
  delayIntervall = 100;

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
  }

  onLogin() {
    if(this.username){
      this.authenticationService.login(this.username, this.password)
      .subscribe({
        next: (person : Person) => {

          this.authenticationService.token(person).subscribe({
            next: (data : any) => {
              this.authenticationService.setCurrentJwtValue(data.token);
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
