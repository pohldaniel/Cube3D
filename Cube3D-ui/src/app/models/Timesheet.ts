export class Timesheet {

  constructor(obj : any){
      if(obj){
        this.id = obj.id;
        this.begin = obj.begin;
        this.end = obj.end;
      }
  }
  
  id!: number;
  begin!: string;
  end!: string;
}