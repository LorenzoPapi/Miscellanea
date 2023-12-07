import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service'
import { apiPokemons, apiPokemon } from '../schema'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public allPokemons: apiPokemon[];

  constructor(private apiService : ApiService) {}

  ngOnInit(): void {
    this.apiService.getPokemons().subscribe(
      (data: apiPokemons) => {
        this.allPokemons = data.results;
        console.log(data);
      },
      error => {
        console.error(error);
      }
    )
  }

}
