import {Component, OnInit,  ElementRef, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {DatabaseService} from '../../services/database.service';
import {SnackService} from '../../services/snack.service';

@Component({
    selector: 'app-releaseItem-durchfuehren',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.sass', './settings.component.scss'],
    imports: [SpinnerComponent, CommonModule],
    standalone: true
})
export class SettingsComponent implements OnInit {

  @ViewChild('toggelLevel', {static: true})  toggelLevel!: ElementRef;

  isWait: boolean = false;
  dataLoaded: boolean = false;
  delayIntervall = 100;

  logLevels: string[] = ['ERROR', 'INFO'];
  logLevel: string = '' ;

  constructor(private readonly databaseService: DatabaseService, private snack: SnackService){

  }

  ngOnInit() {
    this.getLogLevel();
  }

  ngAfterViewInit() {  
    
    setTimeout(() => {  
      if(this.dataLoaded){  
        this.toggelLevel.nativeElement['checked'] = (this.logLevel == 'INFO');
       }else{
        this.ngAfterViewInit()
      }   
    }, 100);
  }

  getLogLevel() {
    setTimeout(()=>{this.isWait = !this.dataLoaded ? true : false}, this.delayIntervall);
    this.dataLoaded = false;
    this.databaseService.logLevel().subscribe(response => {
      this.logLevel = response.message;
      this.dataLoaded = true;
      this.isWait = false;
    }); 
  }

  setLogLevel(level: string){   
    this.databaseService.setLogLevel(this.logLevel).subscribe(() => {
    }); 
  }

  changeLogLevel(event: any){
    this.logLevel = event.target.checked ? this.logLevels[1] : this.logLevels[0];
    this.setLogLevel(this.logLevel);
  }
}