import {Component, Inject, OnInit} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from "@angular/material/dialog";
import {MatDialogModule} from '@angular/material/dialog';

@Component({
  selector: 'confirmDialog-app',
  templateUrl: './confirm.dialog.html',
  styleUrls: ['./confirm.dialog.sass'],
  imports: [MatDialogModule],
  standalone: true
})
export class ConfirmDialog implements OnInit{

  confirm : boolean = false;
  message: string = '';

  constructor(private dialogRef: MatDialogRef<ConfirmDialog>,
              @Inject(MAT_DIALOG_DATA) data: any){
    this.message = data;
  }

  ngOnInit() {
    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === "Escape") {
        this.close();
      }
    });
  }

  action(){
    this.confirm = true;
    this.dialogRef.close(this.confirm);      
  }

  close(){
    this.confirm = false;
    this.dialogRef.close(this.confirm);
  }

}