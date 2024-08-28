import {Component, OnInit, ViewChild, ElementRef} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatSort} from '@angular/material/sort'
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {AuthenticationService} from '../../services/authentication.service';
import {DatabaseService} from '../../services/database.service';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {Timesheet} from '../../models/Timesheet';

@Component({
    selector: 'app-timesheet',
    templateUrl: './timesheet.component.html',
    styleUrls: ['./timesheet.component.sass'],
    imports: [SpinnerComponent, CommonModule],
    standalone: true
})
export class TimesheetComponent implements OnInit {

  isWait: boolean = false;
  dataLoaded: boolean = false;
  delayIntervall = 100;

  displayedColumnsService : String[] = ['id', 'begin', 'end'];
  dataSourceService = new MatTableDataSource<Timesheet>();

  @ViewChild('paginatorService', {static: false}) paginatorService!: MatPaginator;
  @ViewChild('sortService', {static: false}) sortService!: MatSort;
  @ViewChild('filterService', {static: false}) filterInputService!: ElementRef;

  constructor(private readonly databaseService: DatabaseService,
              private readonly authenticationService: AuthenticationService){
  }

  ngOnInit() {

  }

  ngAfterViewInit() {  
    setTimeout(() => {  
      if(this.dataLoaded){  
        this.dataSourceService.sortingDataAccessor = this.sortServiceFunc;    
        this.dataSourceService.sort = this.sortService; 
        
        this.resetFocus();    
      }else{
        this.ngAfterViewInit()
      }   
    }, 100);
  }

  refreshTableService() { 
    setTimeout(()=>{this.isWait = !this.dataLoaded ? true : false}, this.delayIntervall);
    this.dataLoaded = false;
    this.databaseService.getTimesheets().subscribe(response => {
      this.dataSourceService.data = response;
      this.dataLoaded = true;
      this.isWait = false;
      }
    ); 
  }

  resetFocus(){
    
  }

  public sortServiceFunc: any = (item : any, property : string) : string | number => {
    switch(property) {
      //case 'service': return item.name.toLocaleLowerCase();       
      //case 'description': return item.description.toLocaleLowerCase();      
      //case 'application': return item.application.name.toLocaleLowerCase();  
      default: return item[property];
    }
  };
}