import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {MatDialogModule} from '@angular/material/dialog';

@Component({
    selector: 'popUpDialog-app',
    templateUrl: './popUp.dialog.html',
    styleUrls: ['./popUp.dialog.sass'],
    imports: [MatDialogModule],
    standalone: true
})
export class PopUpDialog implements OnInit {

  constructor(private dialogRef: MatDialogRef<PopUpDialog>){}

  ngOnInit() {
    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === "Escape") {
        this.close();
      }
    });
  }

  close() {
    this.dialogRef.close();
  }
}
