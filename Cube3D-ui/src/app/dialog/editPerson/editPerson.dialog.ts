import {Component, OnInit, Inject} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatDialogModule, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {Person} from '../../models/Person';
import {DatabaseService} from '../../services/database.service';
import {SnackService} from '../../services/snack.service';
import {PersonForm} from '../../froms/person.form'

@Component({
    selector: 'editPersonDialog-app',
    templateUrl: './editPerson.dialog.html',
    styleUrls: ['./editPerson.dialog.sass'],
    imports: [MatDialogModule, MatFormFieldModule, FormsModule, MatInputModule, ReactiveFormsModule],
    standalone: true
})
export class EditPersonDialog implements OnInit {

    personForm : PersonForm;
    data : Person;

  constructor(private dialogRef: MatDialogRef<EditPersonDialog>,
              private readonly databaseService: DatabaseService,
              private readonly snackService: SnackService,
              @Inject(MAT_DIALOG_DATA) data: any){
    this.personForm = new PersonForm();
    this.data = data;
  }

  ngOnInit() {
    this.personForm.id.setValue(this.data.id);
    this.personForm.surname.setValue(this.data.surname);
    this.personForm.prename.setValue(this.data.prename);
    this.personForm.mail.setValue(this.data.mail);
    this.personForm.externalCompany.setValue(this.data.externalCompany);
    this.personForm.role.setValue(this.data.role);
    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  close() {
    this.dialogRef.close();
  }

  editPerson(){
    if (this.personForm.valid) {
    const person: Person = new Person({
      id : this.data.id,
      surname : this.personForm.value.surname,
      prename : this.personForm.value.prename,
      mail : this.personForm.value.mail,
      externalCompany : this.personForm.value.externalCompany,
      role : this.personForm.value.role,
         //topicAreas : this.data.topicAreas,
         //passwordHash : this.data.passwordHash,
         //sessionId : this.data.sessionId,
         //sessionIdExpiryDate : this.data.sessionIdExpiryDate
    });
    this.postPerson(person);
    }
  }

  postPerson(person: Person) {
    this.databaseService.updatePerson(person).subscribe({
      next: (data : any) => {
        this.dialogRef.close(data);
      },
      error: (error : HttpErrorResponse) => {
        if(error.status == 400){
          this.snackService.open('ERROR', error.error);
        } else if(error.status == 401){
          this.snackService.open('INFO', error.error);
        }else if(error.status == 409)
          this.snackService.open('INFO', error.error);
        }
    });     
  }
}
