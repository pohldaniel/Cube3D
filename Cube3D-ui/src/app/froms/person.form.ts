import {FormControl, FormGroup, Validators} from '@angular/forms';

export class PersonForm extends FormGroup {

  constructor() {
    super({
      id: new FormControl('', [Validators.required, Validators.minLength(3)]),
      surname: new FormControl('', [Validators.required, Validators.minLength(3)]),
      prename: new FormControl('', [Validators.required, Validators.minLength(3)]),
      mail: new FormControl('', [Validators.required, Validators.email]),
      externalCompany: new FormControl(''),
      role: new FormControl('', [Validators.required])
    });
  }

  get id(): FormControl {
    return this.get('id') as FormControl;
  }

  get surname(): FormControl {
    return this.get('surname') as FormControl;
  }

  get prename(): FormControl {
    return this.get('prename') as FormControl;
  }

  get mail(): FormControl {
    return this.get('mail') as FormControl;
  }

  get externalCompany(): FormControl {
    return this.get('externalCompany') as FormControl;
  }

  get role(): FormControl {
    return this.get('role') as FormControl;
  }
}
