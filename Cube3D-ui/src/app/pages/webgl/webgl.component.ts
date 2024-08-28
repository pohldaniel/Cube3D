import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {AuthenticationService} from '../../services/authentication.service';
import {Person} from '../../models/Person';

@Component({
    selector: 'app-webgl',
    templateUrl: './webgl.component.html',
    styleUrls: ['./webgl.component.sass'],
    imports: [SpinnerComponent, CommonModule],
    standalone: true
})
export class WebglComponent implements OnInit {

  isWait: boolean = false;
  //currentUser: Person;
  constructor(private readonly authenticationService: AuthenticationService){
    //this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {

  }
}