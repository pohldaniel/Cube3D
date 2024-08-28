import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarConfig} from '@angular/material/snack-bar';
import {SnackComponent} from '../shared/snack/snack.component';

export enum $SNACK {
  OK = 'OK',
  ERROR = 'ERROR',
  INFO = 'INFO',
  ND = 'ND'
}

@Injectable({
  providedIn: 'root'
})
export class SnackService {

  constructor(private snack: MatSnackBar) {}

  public open( type: string , msg: string, duration: number = 3000, errors?: [string, string][], singleline: boolean = true) {
    let closeable: boolean = duration == -1;
    const snackconfig = new MatSnackBarConfig();
    snackconfig.duration = duration;
    snackconfig.data = { msg, type , errors, closeable, singleline};
    snackconfig.panelClass = ['info-cls']
    this.snack.openFromComponent( SnackComponent, snackconfig );
  }
}
