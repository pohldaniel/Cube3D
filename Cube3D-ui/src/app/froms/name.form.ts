import {FormControl, FormGroup, Validators} from '@angular/forms';

export class NameForm extends FormGroup {

  constructor() {
    super({
      name: new FormControl('', [Validators.required])
    });
  }

  get name(): FormControl {
    return this.get('name') as FormControl;
  }
}
