import {Component} from '@angular/core';
import {RouterOutlet, Router, ActivatedRoute, NavigationEnd} from '@angular/router';
import {filter, map, mergeMap} from 'rxjs/operators';
import {Observable} from "rxjs";
import {MenuComponent} from './shared/menu/menu.component';
import {ResizableDirective} from './directives/resizeable.directives';
import {CommonModule} from '@angular/common';
import {FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {faEdit as farEdit, faEye as farEye, faCalendar as farCalendar, faCalendarAlt as farCalendarAlt, faTrashAlt as farTrashAlt, faSave as farSave} from '@fortawesome/free-regular-svg-icons';
import {faLock as fasLock, faLockOpen as fasLockOpen} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MenuComponent, ResizableDirective, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
})
export class AppComponent {
  showSidebar$: Observable<boolean>;
  private defaultShowSidebar = true;

  constructor(
      public router: Router,
      private activatedRoute: ActivatedRoute,
      private library: FaIconLibrary){
      library.addIcons(farCalendar, farSave, farEye, farEdit, farCalendarAlt, farTrashAlt, fasLock, fasLockOpen);  
     
      //immediately logout
      this.showSidebar$ = this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map(() => activatedRoute),
      map(route => {      
          while (route.firstChild) {
            route = route.firstChild;
          }
          return route;
        }),
        mergeMap(route => route.data),
        map(data => data.hasOwnProperty('showSidebar') ? data['showSidebar'] : this.defaultShowSidebar),       
      )
    }      
}
