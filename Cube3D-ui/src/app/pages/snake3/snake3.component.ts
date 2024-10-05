import {Component, ViewChild,ElementRef, NgZone, HostListener} from '@angular/core';
import {EmscriptenWasmComponent} from "../emscripten-wasm.component";

@Component({
  selector: 'app-snake-3',
  templateUrl: './snake3.component.html',
  styleUrls: ['./snake3.component.sass'],
  standalone: true,
})
export class Snake3Component extends EmscriptenWasmComponent {
  @ViewChild("canvas") canvas!: ElementRef;
  error!: string;
  constructor(private ngZone: NgZone) {
    super("SnakeModule",  'assets/wasm/Snake3/snake_module.js', 'assets/wasm/Snake3/snake_module.wasm', 'assets/wasm/Snake3/snake_module.data');
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
      };
    };
  } 

  start(element: HTMLParagraphElement){
    element.style.visibility = "hidden";
    this.module.ccall!('mainf', 'void', ['void']);
  }
}