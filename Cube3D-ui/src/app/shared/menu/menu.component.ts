import {Component, OnInit, ElementRef} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {DomSanitizer}from '@angular/platform-browser';
import {MatIconRegistry, MatIconModule} from '@angular/material/icon';
import {AuthenticationService} from '../../services/authentication.service';
import {Role} from '../../models/Role.enum';

export interface Settings {
  id: number;
  favorites: number;
}

export interface MenuItem {
  target: string;
  description: string;
  description2: string;
  class : string;
  index: number;
}
  
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.sass'],
  imports: [MatIconModule, CommonModule],
  standalone: true,
})
export class MenuComponent implements OnInit{

  currentRoles : Role[];

  public checked : boolean[] =[];
  public favoriteList : MenuItem[] =[];
  favorites: number = 0;
  settings: Settings = {id: 0, favorites: 0};

  targetedSubmenu: string = "one";
  targetedSubSubmenu: string = "";
  storeTargetedSubmenu: string = "";
  storeTargetedSubSubmenu: string = "";
  targetedSubSubSubmenu: string = "";
  storeTargetedSubSubSubmenu: string = "";

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private elementRef: ElementRef,
    public router: Router,
    readonly authenticationService: AuthenticationService) {
    this.elementRef.nativeElement.style.setProperty('--menu-width', '200px');              
    this.matIconRegistry.addSvgIcon("custom_check", this.domSanitizer.bypassSecurityTrustResourceUrl("./assets/svg/stop.svg"));
    this.matIconRegistry.addSvgIcon("custom_menu",this.domSanitizer.bypassSecurityTrustResourceUrl("./assets/svg/hamburger.svg"));
    this.matIconRegistry.addSvgIcon("custom_star",this.domSanitizer.bypassSecurityTrustResourceUrl("./assets/svg/star.svg"));
    this.currentRoles = this.authenticationService.getCurrentRoles();
  }

  ngOnInit() {
    if(this.router.url == "/dashboard" || this.router.url == "/timesheet" || this.router.url == "/help"){
      this.targetedSubmenu = "two";
      this.storeTargetedSubmenu = "two";     
    }

    if(this.router.url == "/webgl"){
      this.targetedSubmenu = "three";
      this.storeTargetedSubmenu = "three";     
    }

    if(this.router.url == "/person"){
      this.targetedSubmenu = "four";
      this.storeTargetedSubmenu = "four";     
    }
  }

  ngAfterViewInit(){
    this.refreshItems();       
  }

  refreshItems(){
    this.favoriteList = [];
    this.checked = [];
    let map = new Map<number, MenuItem>()
    .set(Math.pow(2, 0), {target : '/dashboard', description: 'Dashboard', description2: '', class : 'two-one', index : 0} as MenuItem)
    .set(Math.pow(2, 1), {target : '/timesheet', description: 'Zeiterfassung', description2: '', class : 'two-two', index : 1} as MenuItem)
    .set(Math.pow(2, 2), {target : '/help', description: 'Hilfe', description2: '', class : 'two-three', index : 2} as MenuItem)

    .set(Math.pow(2, 3), {target : '/webgl', description: 'Web GL', description2: '', class : 'three-one', index : 3} as MenuItem)
    .set(Math.pow(2, 6), {target : '/wasm', description: 'WebAssembly', description2: '', class : 'three-two', index : 6} as MenuItem)

    .set(Math.pow(2, 4), {target : '/person', description: 'Benutzerverwaltung', description2: '', class : 'four-one', index : 4} as MenuItem)
    .set(Math.pow(2, 5), {target : '/settings', description: 'Einstellungen', description2: '', class : 'four-two', index : 5} as MenuItem);
   
    for(let key of map.keys()) {

      if((this.settings.favorites | key) == this.settings.favorites){
        this.checked.push(true);       
        this.favoriteList.push(map.get(key) as MenuItem);
      }else{
        this.checked.push(false);
      }  
    }
  }

  onClick(event: any) {  
    this.targetedSubmenu = event.target.parentElement.id != "" ? event.target.parentElement.id  : event.target.parentElement.parentElement.id != "" ? event.target.parentElement.parentElement.id : event.target.parentElement.parentElement.parentElement.id;
    
    if(this.targetedSubmenu.includes('-') && !this.targetedSubmenu.includes('favorite-')){
      return;
    }

    if(this.targetedSubmenu === this.storeTargetedSubmenu && this.targetedSubmenu !== ''){
      this.targetedSubmenu = '';
      this.storeTargetedSubmenu = '';
      return;
    }

    this.targetedSubSubmenu = '';
    this.storeTargetedSubSubmenu = '';

    this.targetedSubSubSubmenu = '';
    this.storeTargetedSubSubSubmenu = '';

    if(this.targetedSubmenu.includes('favorite-')){
      let id = this.targetedSubmenu;
      this.storeTargetedSubSubSubmenu =  id.replace('favorite-', '') 

      let array = this.storeTargetedSubSubSubmenu.split('-');

      if(array.length == 4){
        this.storeTargetedSubSubSubmenu = array[0] + '-' + array[1] + '-' + array[2];
        this.storeTargetedSubSubmenu = array[0] + '-' + array[1];
      }
      
      if(array.length == 3){
        this.storeTargetedSubSubmenu = array[0] + '-' + array[1];
      }

      this.targetedSubmenu = array[0];
    }

    if(this.targetedSubmenu.includes('-')){
      this.targetedSubmenu = this.targetedSubmenu.split('-')[0];  
    }

    if(this.targetedSubmenu == ''){
      this.targetedSubmenu = this.storeTargetedSubmenu;
    }else{
      this.storeTargetedSubmenu = this.targetedSubmenu;
    }
    return;
  }

  onClickSub(event: any) {  
    this.targetedSubSubmenu = event.target.parentElement.id != "" ? event.target.parentElement.id  : event.target.parentElement.parentElement.id != "" ? event.target.parentElement.parentElement.id : event.target.parentElement.parentElement.parentElement.id;

    if(this.targetedSubSubmenu == this.storeTargetedSubSubmenu && this.targetedSubSubmenu != ''){
      this.targetedSubSubmenu = '';
      this.storeTargetedSubSubmenu = '';
    
      this.targetedSubSubSubmenu = '';
      this.storeTargetedSubSubSubmenu = '';
      return;
    }

    if(this.targetedSubSubmenu.includes('-')){
      this.targetedSubSubmenu = this.targetedSubSubmenu.split('-')[0] + '-' + this.targetedSubSubmenu.split('-')[1];  
    }

    if(this.targetedSubSubmenu == ''){
      this.targetedSubSubmenu = this.storeTargetedSubSubmenu;
    }else{
      this.storeTargetedSubSubmenu = this.targetedSubSubmenu;
    }
    return;
  }

  onClickSubSub(event: any) {  
    this.targetedSubSubSubmenu = event.target.parentElement.id != "" ? event.target.parentElement.id  : event.target.parentElement.parentElement.id != "" ? event.target.parentElement.parentElement.id : event.target.parentElement.parentElement.parentElement.id;

    if(this.targetedSubSubSubmenu == this.storeTargetedSubSubSubmenu && this.targetedSubSubSubmenu != ''){
      this.targetedSubSubSubmenu = '';
      this.storeTargetedSubSubSubmenu = '';
      return;
    }

    if(this.targetedSubSubSubmenu.includes('-')){
      this.targetedSubSubSubmenu = this.targetedSubSubSubmenu.split('-')[0] + '-' + this.targetedSubSubSubmenu.split('-')[1]+ '-' + this.targetedSubSubSubmenu.split('-')[2];  
    }

    if(this.targetedSubSubSubmenu == ''){
      this.targetedSubSubSubmenu = this.storeTargetedSubSubSubmenu;
    }else{
      this.storeTargetedSubSubSubmenu = this.targetedSubSubSubmenu;
    }
    return;
  }

  updateFavorites(event: any, _number: number, checked: boolean, menuItem?: MenuItem){
    let settings: Settings = this.settings;
    let number = menuItem ? Math.pow(2, menuItem.index) : Math.pow(2, _number);
    if(checked){       
      settings.favorites = settings.favorites ^ number;
    }else{
      settings.favorites = settings.favorites | number;        
    }
    this.postSettings(settings);        
  }

  postSettings(settings: Settings) {      
    this.refreshItems();
  }

  navigate(route: string){
    this.router.navigateByUrl(route);
  }

  getAccessAdmin() : boolean{
    return this.currentRoles.some(e => e === Role.ADMIN);
  }

  getAccessPersonManager() : boolean{
    return this.currentRoles.some(e => e === Role.PERSON_MANAGER);
  }
}
  