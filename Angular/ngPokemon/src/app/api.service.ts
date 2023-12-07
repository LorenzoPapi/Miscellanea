import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  constructor(private httpClient : HttpClient) {}

  public getUrl(url: any) {
    return this.httpClient.get(url)
  }

  public getMoves() {
    return this.httpClient.get("https://pokeapi.co/api/v2/move/?limit=9999")
  }

  public getAllUrls() {
    /*var urls = newArray(746)
    for (var i = 1; i <= 728; i++) {
      urls[i-1] = this.httpClient.get("https://pokeapi.co/api/v2/move/" + i)
    }
    for (var i = 10001; i <= 10018; i++) {
      urls[i-9273] = this.httpClient.get("https://pokeapi.co/api/v2/move/" + i)
    }
    return urls*/
  }

  public getMove(move : any) {
    return this.httpClient.get("https://pokeapi.co/api/v2/move/" + move.toLowerCase());
  }

  public getPokemon(pokemon: any) {
    return this.httpClient.get("https://pokeapi.co/api/v2/pokemon/" + pokemon.toLowerCase());
  }

  public getTypeChart() {
    return this.httpClient.get("assets/types.json");
  }

  public getTranslations() {
    return this.httpClient.get("assets/translations.json");
  }
}

