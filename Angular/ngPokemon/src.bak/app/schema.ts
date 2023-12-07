export class apiPokemon {
    name:string;
    url:string;
}

export class apiPokemons {
  count: number;
  next: any;
  previous: any;
  results: apiPokemon[];
}