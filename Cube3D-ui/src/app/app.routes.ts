import { Routes } from '@angular/router';
import {HelpComponent} from './pages/help/help.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {ErrorPageComponent} from './pages/errorPage/errorpage.component';
import {LoginComponent} from './pages/login/login.component';
import {LoginNewComponent} from './pages/loginNew/loginNew.component';
import {TimesheetComponent} from './pages/timesheet/timesheet.component';
import {PersonComponent} from './pages/person/person.component';
import {SettingsComponent} from './pages/settings/settings.component';
import {WebglComponent} from './pages/webgl/webgl.component';
import {authGuard} from './guard/auth.guard';
import {Role, RoleList} from './models/Role.enum';

export const routes: Routes = [
  { path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: RoleList },
  },  
  { path: 'timesheet', 
    component: TimesheetComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: RoleList }
  },  
  { path: 'help', 
    component: HelpComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: RoleList  }
  },  
  { path: 'webgl', 
    component: WebglComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: RoleList  }
  },  
  { path: 'person', 
    component: PersonComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: [Role.ADMIN, Role.PERSON_MANAGER] }
  }, 
  { path: 'settings', 
    component: SettingsComponent,
    canActivate: [authGuard],
    data : {showSidebar: true, Roles: [Role.ADMIN, Role.PERSON_MANAGER] }
  }, 
  {
    path: 'gateway',
    component: LoginComponent,
    data : {showSidebar: false}
  },
  {
    path: 'errorpage',
    component: ErrorPageComponent,
    data : {showSidebar: false}
  },
  {
    path: '',
    component: LoginComponent,
    data : {showSidebar: false}
  },
  {
    path: '**',
    redirectTo: ''
  } 
];
