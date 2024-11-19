import {Component, OnInit, AfterViewInit,ViewChild,ElementRef, OnDestroy, NgZone} from '@angular/core';
import {EmscriptenWasmComponent} from "../emscripten-wasm.component";

@Component({
  selector: 'app-snake',
  templateUrl: './snakec.component.html',
  styleUrls: ['./snakec.component.sass'],
  standalone: true,
})
export class SnakecComponent extends EmscriptenWasmComponent {
  @ViewChild("canvas") canvas!: ElementRef;
  error!: string;
  constructor(private ngZone: NgZone) {
    super("SnakeModule",  'assets/wasm/snakec/snake_c.js', 'assets/wasm/snakec/snake_c.wasm');
    this.moduleDecorator = (mod) => {
      mod.canvas = <HTMLCanvasElement>this.canvas.nativeElement;
      mod.printErr = (what: string) => {
        if (!what.startsWith("WARNING")) {
          this.ngZone.run(() => (this.error = what));
        }
      };
    };
  } 
}