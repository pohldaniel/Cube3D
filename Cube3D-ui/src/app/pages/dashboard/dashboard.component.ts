import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {AuthenticationService} from '../../services/authentication.service';
import {Person} from '../../models/Person';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.sass'],
    imports: [SpinnerComponent, CommonModule],
    standalone: true
})
export class DashboardComponent implements OnInit {

  isWait: boolean = false;

  constructor(private readonly authenticationService: AuthenticationService){

  }

  ngOnInit() {

  }
}