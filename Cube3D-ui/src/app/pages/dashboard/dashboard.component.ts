import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {AuthenticationService} from '../../services/authentication.service';
import {Role} from '../../models/Role.enum';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.sass'],
    imports: [SpinnerComponent, CommonModule],
    standalone: true
})
export class DashboardComponent implements OnInit {

  isWait: boolean = false;
  currentRoles : Role[];
  
  constructor(private readonly authenticationService: AuthenticationService){
    this.currentRoles = this.authenticationService.getCurrentRoles();
  }

  ngOnInit() {

  }
}