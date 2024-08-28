import {Component, OnInit, ViewChild, ElementRef, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatDialogModule} from '@angular/material/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {NameForm} from '../../froms/name.form';

@Component({
  selector: 'infoDialog-app',
  templateUrl: './info.dialog.html',
  styleUrls: ['./info.dialog.sass'],
  imports: [MatDialogModule, MatFormFieldModule, FormsModule, MatInputModule, ReactiveFormsModule],
  standalone: true
})
export class InfoDialog implements OnInit {

  value: String = "Test";
  nameForm : NameForm;
  @ViewChild('hook') matFormField: any;

  constructor(private dialogRef: MatDialogRef<InfoDialog>, private renderer: Renderer2){
    this.nameForm = new NameForm();
    this.nameForm.name.setValue('');
  }

  ngOnInit() {
    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === "Escape") {
        this.close();
      }
    });
   
  }

  ngAfterViewInit(){
    //this.renderer.addClass(this.matFormField._elementRef.nativeElement.children[0], 'mdc-text-field--invalid');
  }

  close() {
    this.dialogRef.close();
  }

  save() {
    this.dialogRef.close();
  
  }
}
