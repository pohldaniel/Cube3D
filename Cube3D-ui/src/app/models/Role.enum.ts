export enum Role {
  USER = 'USER',
  TOPICAREA_MANAGER = 'TOPICAREA_MANAGER',
  ACTION_MANAGER = 'ACTION_MANAGER',
  ADMIN = 'ADMIN',
  PERSON_MANAGER = 'PERSON_MANAGER'
}

export const RoleList : Role[] = Object.entries(Role)
.map(([key, value]) => value); 


  