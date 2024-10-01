//https://github.com/LukasMarx/angular-wasm/issues/2
//emcc -O3 wasm/fibonacci.c -o wasm/fibonacci.js -s WASM=1 -s MODULARIZE=1
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
declare var Module: any;
//import * as Module from '../../../wasm/fibonacci.js';
//import * as variable from 'fibonacci';
//import wasmModule from "../../assets/wasm/fibonacci.js";

@Injectable({
  providedIn: 'root'
})
export class FibonacciWasmService {
  module: any;

  public wasmReady$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );

  constructor() {
    this.instantiateWasm('/assets/wasm/fibonacci.wasm');
  }

  private async instantiateWasm(url: string) {
    // fetch the wasm file
    const wasmFile = await fetch(url);

    // convert it into a binary array
    const buffer = await wasmFile.arrayBuffer();
    const binary = new Uint8Array(buffer);

    // create module arguments
    // including the wasm-file
    const moduleArgs = {
      wasmBinary: binary,
      onRuntimeInitialized: () => {
        this.wasmReady$.next(true);
      }
    };
    // instantiate the module
    this.module = Module(moduleArgs);
  }

  public fibonacci(input: number): number {
     //return this.module.__zone_symbol__value.ccall('fibonnaci','number',['number'], [input])
    return this.module.__zone_symbol__value._fibonacci(input);
  }

  /*changeColor(){
    changeBgColor(this.dateElem?.nativeElement);
  }*/
}
