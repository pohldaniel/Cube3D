import {Person} from './Person';

export class TopicArea {

  constructor(obj : any){

    if(obj){
      this.name = obj.name;
      this.persons = obj.persons;
    }
  }

  name!: string;
  persons!: Array<Person>;
}
