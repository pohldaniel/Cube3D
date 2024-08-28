import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatDialogModule} from '@angular/material/dialog';
import {HttpErrorResponse} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {Person} from '../../models/Person';
import {DatabaseService} from '../../services/database.service';
import {SnackService} from '../../services/snack.service';
import {PersonForm} from '../../froms/person.form'

@Component({
    selector: 'newPersonDialog-app',
    templateUrl: './newPerson.dialog.html',
    styleUrls: ['./newPerson.dialog.sass'],
    imports: [MatDialogModule, MatFormFieldModule, FormsModule, MatInputModule, ReactiveFormsModule],
    standalone: true
})
export class NewPersonDialog implements OnInit {

  personForm : PersonForm;

  constructor(private dialogRef: MatDialogRef<NewPersonDialog>,
              private readonly databaseService: DatabaseService,
              private readonly snackService: SnackService){
    this.personForm = new PersonForm();
  }

  ngOnInit() {
    this.personForm.id.setValue('user1');
    this.personForm.surname.setValue('Peter');
    this.personForm.prename.setValue('Panzer');
    this.personForm.mail.setValue('test@test.de');
    this.personForm.externalCompany.setValue('N.N.');
    this.personForm.role.setValue('USER');
    this.dialogRef.keydownEvents().subscribe(event => {
        if (event.key === 'Escape') {
          this.close();
        }
      });
  }

  close() {
    this.dialogRef.close();
  }

  addNewPerson(){
    if (this.personForm.valid) {
      const person: Person = new Person({
        id : this.personForm.value.id,
        surname : this.personForm.value.surname,
        prename : this.personForm.value.prename,
        mail : this.personForm.value.mail,
        externalCompany : this.personForm.value.externalCompany,
        role : this.personForm.value.role});
      this.postPerson(person);
    }
  }

  postPerson(person: Person) {
    this.databaseService.createPerson(person).subscribe({
      next: (data : any) => {
        this.dialogRef.close(data);
      },
      error: (error : HttpErrorResponse) => {
        if(error.status == 400){
          this.snackService.open('ERROR', error.error.message);
        } else if(error.status == 401){
          this.snackService.open('INFO', error.error.message);
        }else if(error.status == 409)
          this.snackService.open('INFO', error.error.message);
        }
    });      
  }
}
