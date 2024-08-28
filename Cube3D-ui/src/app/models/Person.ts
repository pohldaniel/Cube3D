import {Role} from './Role.enum';
import {TopicArea} from './TopicArea';
import {environment} from '../../environments/environment';

export class Person {
  
  constructor(obj: any){
    if(obj){
      this.id = obj.id;
      this.surname = obj.surname;
      this.prename = obj.prename;
      this.mail = obj.mail;
      this.externalCompany = obj.externalCompany;
      this.role = obj.role;
      this.topicAreas = obj.topicAreas;
      this.passwordResetLink = obj.passwordResetToken ? environment.passwordUrl + '?token=' + obj.passwordResetToken : null;
    }
  }

  id!: string;
  surname!: string;
  prename!: string;
  mail!: string;
  externalCompany!: string;
  role!: Role;
  topicAreas!: Array<TopicArea>;
  passwordResetLink!: string | null;
}
