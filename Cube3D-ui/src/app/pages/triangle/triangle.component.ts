import {Component, OnInit, AfterViewInit, OnDestroy} from '@angular/core';
import {EmscriptenWasmComponent} from '../emscripten-wasm.component';
declare var Module: any;

@Component({
  selector: 'app-triangle',
  templateUrl: './triangle.component.html',
  styleUrls: ['./triangle.component.scss'],
  standalone: true,
})
export class TriangleComponent extends EmscriptenWasmComponent implements OnInit, AfterViewInit, OnDestroy {
 
  constructor() {
    super("TriangleModule",  'assets/wasm/triangle.js', 'assets/wasm/triangle.wasm');
  }

  ngOnInit() {
    
  }

  override ngAfterViewInit() {
    super.ngAfterViewInit();
  }
  
  ngOnDestroy() {  
  
  }
}