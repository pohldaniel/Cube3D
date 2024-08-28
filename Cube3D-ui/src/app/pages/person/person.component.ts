import {Component, Renderer2, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {DomSanitizer}from '@angular/platform-browser';
import {MatIconRegistry, MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {NewPersonDialog} from '../../dialog/newPerson/newPerson.dialog';
import {EditPersonDialog} from '../../dialog/editPerson/editPerson.dialog';
import {ConfirmDialog} from '../../dialog/confirm/confirm.dialog';
import {SpinnerComponent} from '../../shared/spinner/spinner.component';
import {SnackService} from '../../services/snack.service';
import {DatabaseService} from '../../services/database.service';
import {AuthenticationService} from '../../services/authentication.service';
import {Person} from '../../models/Person';
import {Role} from '../../models/Role.enum';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

@Component({
    selector: 'app-person',
    templateUrl: './person.component.html',
    styleUrls: ['./person.component.sass'],
    imports: [SpinnerComponent, CommonModule, FormsModule, MatInputModule, MatIconModule, MatTableModule, FontAwesomeModule, MatTooltipModule],
    standalone: true
})
export class PersonComponent implements OnInit {


  currentRoles : Role[];
  selectedPerson!: Person;

  isWait: boolean = false;
  dataLoaded: boolean = false;
  delayIntervall = 100;

  displayedColumns : String[] = [ 'id', 'passwordResetLink', 'actions'];
  dataSource = new MatTableDataSource<Person>();

  constructor(
    private readonly databaseService: DatabaseService,
    public readonly authenticationService: AuthenticationService,
    private readonly snackService: SnackService,
    private readonly matIconRegistry: MatIconRegistry,
    private readonly domSanitizer: DomSanitizer,
    private readonly dialog: MatDialog){
      this.currentRoles = this.authenticationService.getCurrentRoles();
      this.matIconRegistry.addSvgIcon("custom_delete", this.domSanitizer.bypassSecurityTrustResourceUrl('./assets/svg/delete.svg'));
  }

  ngOnInit() {   
    this.refreshTable();
  }

  refreshTable() {
    setTimeout(()=>{this.isWait = !this.dataLoaded ? true : false}, this.delayIntervall);
    this.dataLoaded = false;
    this.databaseService.getPersons().subscribe(response => {
      this.dataSource.data = response;
      this.dataLoaded = true;
      this.isWait = false;
    }); 
  }

  register(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    this.dialog.open(NewPersonDialog , dialogConfig).afterClosed().subscribe({
      next: (response : any ) => {
        this.refreshTable();
      },
      error: (error: HttpErrorResponse) => {
        this.snackService.open('ERROR', error.error.message);
      }
    });  
  }   

  editPerson(person: Person) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = person;
    this.dialog.open(EditPersonDialog, dialogConfig).afterClosed().subscribe({
      next: (response : any ) => {
        if(response.status == 200){
          this.snackService.open('OK', 'Person edited');
          this.refreshTable();
        }
      },
      error: (error: any) => {
        this.snackService.open('ERROR', error.error.message);
      }
    });  
  }
  
  deletePerson(person: Person) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = person.id + ' wirklich entfernen?';
    this.dialog.open(ConfirmDialog, dialogConfig).afterClosed().subscribe(
      (confirm: boolean) => {
        if(confirm){
          this.selectedPerson = person;
          this.databaseService.deletePerson(this.selectedPerson).subscribe({
            next: (response : any ) => {
              if(response.status == 200){
                this.snackService.open('OK', 'Person entfernt');
                this.refreshTable();
              }

              if(response.status == 403){
                this.snackService.open('INFO', response.message);
              }
            },
            error: (error: HttpErrorResponse) => {
              this.snackService.open('ERROR', error.error);
            }
          });
        }
      }
    );  
  }
  
  genrateLink(person: Person){
    this.databaseService.generateLink(person).subscribe({
      next: (response : any ) => {
        this.refreshTable();
      },
      error: (error: HttpErrorResponse) => {
        this.snackService.open('ERROR', error.error);
      }
    })
  }

  removeLink(person: Person){
    this.databaseService.removeLink(person).subscribe({
      next: (response : any ) => {
        this.refreshTable();
      },
      error: (error: HttpErrorResponse) => {
        this.snackService.open('ERROR', error.error);
      }
    })
  }
}