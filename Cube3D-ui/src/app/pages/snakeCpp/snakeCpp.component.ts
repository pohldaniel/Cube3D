import {Component, OnInit, AfterViewInit,ViewChild,ElementRef, OnDestroy, NgZone} from '@angular/core';
import {EmscriptenWasmComponent} from "../emscripten-wasm.component";

@Component({
  selector: 'app-snake-cpp',
  templateUrl: './snakeCpp.component.html',
  styleUrls: ['./snakeCpp.component.sass'],
  standalone: true,
})
export class SnakeCppComponent extends EmscriptenWasmComponent {
  @ViewChild("canvas") canvas!: ElementRef;
  error!: string;
  constructor(private ngZone: NgZone) {
    super("SnakeModule",  'assets/wasm/snake.js', 'assets/wasm/snake.wasm', 'assets/wasm/snake.data');
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