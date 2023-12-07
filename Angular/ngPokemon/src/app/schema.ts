import { FormControl } from '@angular/forms';

export class apiType {
  name:string;
  url:string;
}

export class apiMoves {
  results:apiType[]
}

export class apiMove {
  power:number;
  type:apiType;
  damage_class:apiType;
  typename:string;
  names:apiLang[]
  id:number
}

export class apiFullType {
  slot:number;
  type:apiType;
}

export class apiPokemon {
  types:apiFullType[]
}

class apiLang {
  name:string
  language:apiType
}

class type {
  name:string;
  strong:string[];
  useless:string[];
  weak:string[];
}

class translation {
  string:string
  t:string
}

export class typeChart {
  types:type[]
}

export class translations {
  ts:translation[]
}

export class form {
  name:string;
  nameFight:string;
  type1:string;
  type2:string;
  level:number;
  atk:number;
  def:number;
  type1Fight:string;
  type2Fight:string;
  move:string
  isValid:boolean
}