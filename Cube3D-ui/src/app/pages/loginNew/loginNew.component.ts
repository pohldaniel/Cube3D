import {Component,OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './loginNew.component.html',
  styleUrl: './loginNew.component.css'
})
export class LoginNewComponent implements OnInit {

  returnUrl: string = '';
  username: string = 'Current User';
  password: string = 'erwischt';

  constructor( 
    private readonly route: ActivatedRoute,
    private readonly router: Router, 
    private readonly authenticationService: AuthenticationService,) {
  }

  ngOnInit(){   
    //if (this.authenticationService.currentUserValue) {
         //this.router.navigate(['/dashboard']);
    //}
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';   
  }

  onLogin() {
    if(this.username != ''){
        //this.authenticationService.setCurrentUserValue(this.username);
        let path : string = this.returnUrl === '/' ? '/dashboard': this.returnUrl;
        this.router.navigate([path]);
    }
  }
}
