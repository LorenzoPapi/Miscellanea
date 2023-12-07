import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  constructor(private httpClient : HttpClient) {}

  public getPokemons() {
    return this.httpClient.get("https://pokeapi.co/api/v2/pokemon?limit=9999");
  }
}

