import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatIconRegistry} from "@angular/material/icon"
import {DomSanitizer}from "@angular/platform-browser";

@Component({
  selector: 'app-errorpage',
  templateUrl: './errorpage.component.html',
  styleUrls: ['./errorpage.component.sass'],
  standalone: true,
})
export class ErrorPageComponent implements OnInit {

  isWait: boolean = true;
  dataLoaded: boolean = false;
  delayIntervall = 100;

  constructor(){
  } 

  ngOnInit() {   
    this.refreshTable();
  }

  refreshTable() {
    setTimeout(()=>{this.isWait = !this.dataLoaded ? true : false}, this.delayIntervall);
    this.dataLoaded = false;
  }
}

