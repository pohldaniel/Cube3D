import {Component, ViewChild,ElementRef, NgZone, HostListener} from '@angular/core';
import {EmscriptenWasmComponent} from "../emscripten-wasm.component";

@Component({
  selector: 'app-owl',
  templateUrl: './owl.component.html',
  styleUrls: ['./owl.component.sass'],
  standalone: true,
})
export class OwlComponent extends EmscriptenWasmComponent {
  @ViewChild("canvas") canvas!: ElementRef;
  error!: string;
  constructor(private ngZone: NgZone) {
    super("OwlModule",  'assets/wasm/Owl/naked_owl_module.js', 'assets/wasm/Owl/naked_owl_module.wasm', 'assets/wasm/Owl/naked_owl_module.data');
    this.moduleDecorator = (mod) => {   
      mod.canvas = <HTMLCanvasElement>this.canvas.nativeElement;
      mod.printErr = (what: string) => {
        if (!what.startsWith("WARNING")) {
          this.ngZone.run(() => (this.error = what));
        }
      },
      mod.onRuntimeInitialized = () =>{
        var e = document.getElementById('loadingDiv');
        e!.style.visibility = 'hidden';
      }
    };
  }

  start(element: HTMLParagraphElement){
    element.style.visibility = "hidden";
    document.getElementById("fullScreenButton")!.style.visibility="visible";
    this.module.ccall!('mainf', 'void', ['void']);
  }

  setSize(element: HTMLCanvasElement){
    element.requestFullscreen();
  }

  @HostListener('document:fullscreenchange', ['$event'])
  EventHandler($event : Event) {
    if (!document.fullscreenElement){
        //this.module._setSize(600, 400);
        this.module.ccall!('setSize', 'void', ['number', 'number'], [600, 400]);
    }else{
        //this.module._setSize(window.innerWidth * 0.8,window.innerHeight);
        this.module.ccall!('setSize', 'void', ['number', 'number'], [window.innerWidth * 0.8, window.innerHeight]);
    }	
  }
}