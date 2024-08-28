import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {SnackService} from '../../services/snack.service';
import {PopUpDialog} from '../../dialog/popUp/popUp.dialog';
import {InfoDialog} from '../../dialog/info/info.dialog';
import {ConfirmDialog} from '../../dialog/confirm/confirm.dialog';

@Component({
    selector: 'app-help',
    templateUrl: './help.component.html',
    styleUrls: ['./help.component.sass'],
    standalone: true,
})
export class HelpComponent implements OnInit {

  constructor(private snackService: SnackService, private dialog: MatDialog){}

  ngOnInit() {}

  ngAfterViewInit() {
    this.resetFocus();
  }

  resetFocus() {   
  }
  
  openPopUpDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.panelClass = ['full-width-dialog'];
    this.dialog.open(PopUpDialog , dialogConfig).afterClosed().subscribe();  
  }

  openConfirmDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = 'Service wirklich entfernen?'
    this.dialog.open(ConfirmDialog , dialogConfig).afterClosed().subscribe(
      (confirm: boolean) => {
        if(confirm){
          this.snackService.open('OK', "Service entfernt", 3000); 
        }else{
          this.snackService.open('INFO', "Service konnte nicht entfernt werden", 3000); 
        }
      }
    );  
  }

  openInfoDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = "600px";
    dialogConfig.width = "900px";
    this.dialog.open(InfoDialog , dialogConfig).afterClosed().subscribe();  
  }
}