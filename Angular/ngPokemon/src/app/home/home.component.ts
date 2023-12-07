import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service'
import { apiMoves, apiMove, apiPokemon, form, typeChart, translations } from "../schema";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  pokemonForm: form = {
    name:'',
    nameFight:'',
    type1:'',
    type2:'',
    level:null,
    atk:null,
    def:null,
    type1Fight:'',
    type2Fight:'',
    move:'',
    isValid:false
  };
  dbMove: apiMove = new apiMove;
  dbPokemon: apiPokemon = new apiPokemon;
  pokeGen8: boolean = false;
  critic: boolean = false;
  burning: boolean = false;
  burn: number = 1.0;
  STAB: number = 1.0;
  efficiency: number = 1;
  result: number = 0;
  trans: translations;
  atkString: string = "Attacco"
  defString: string = "Difesa"
  myStorage = localStorage.length == 0 ? null : localStorage
  autoCompleteConfig = {
    'placeholder': 'Mossa',
    'sourceField': ['name']
  }

  constructor(private apiService : ApiService) {}

  onSelect(item : any) {
    
  }

  getRand() : number {
    return Math.random() * 0.15 + 0.85;
  }

  checkValid() {
    var flag = this.pokemonForm.atk != null && this.pokemonForm.def != null && this.pokemonForm.level != null && this.pokemonForm.move != '' && this.pokemonForm.name != '' && this.pokemonForm.type1Fight != '' && this.pokemonForm.nameFight != ''
    this.pokemonForm.isValid = this.pokeGen8 ? flag && this.pokemonForm.type1 != '' : flag
    if (this.pokemonForm.atk <= 0) {
      this.pokemonForm.atk = null
    }
    if (this.pokemonForm.def <= 0) {
      this.pokemonForm.def = null
    }
    if (this.pokemonForm.level == 0) {
      this.pokemonForm.level = null
    }
    for (let i = 0; i < localStorage.length; i++) {
      let key = localStorage.key(i);
      let value = localStorage.getItem(key);
      if (this.pokemonForm.move.toLowerCase() == value.toLowerCase()) {
        this.apiService.getMove(key).subscribe(
          (data: apiMove) => {
            this.atkString = data.damage_class.name == "special" ? "Attacco Speciale" : "Attacco"
            this.defString = data.damage_class.name == "special" ? "Difesa Speciale" : "Difesa"
          }
        )
        break
      }
    }
    console.log(this.pokemonForm)
  }

  onSubmit() : void {
    if (this.pokeGen8 === false) {
      this.apiService.getPokemon(this.pokemonForm.name).subscribe(
        (data : apiPokemon) => {
          this.dbPokemon.types = data.types;
          for (var i = 0; i < this.dbPokemon.types.length; i++) {
            if (this.dbPokemon.types[i].type.name === this.dbMove.typename) {
              this.STAB = 1.5;
              break
            }
          }
        },
        error => {
          console.error(error);
        }
      );
    } else if (this.pokemonForm.type1 === this.dbMove.typename || this.pokemonForm.type2 === this.dbMove.typename)
        this.STAB = 1.5;
    
    this.apiService.getMove(this.getMoveLocalized()).subscribe(
      (data : apiMove) => {
        this.dbMove.power = data.power;
        this.dbMove.typename = data.type.name;
        this.dbMove.damage_class = data.damage_class
        if (this.burning && this.dbMove.damage_class.name == "physical") {
          this.burn = 0.5
        }
      },
      error => {
        console.error(error);
      }
    );
    this.apiService.getTypeChart().subscribe(
      (data : typeChart) => {
        var types = data.types;
        for (var i = 0; i < types.length; i++) {
          var type = types[i]
          if (type.name === this.dbMove.typename) {
            if (type.useless.includes(this.pokemonForm.type1Fight) || type.useless.includes(this.pokemonForm.type2Fight)) {
              this.efficiency = 0;
            }
            if (type.weak.includes(this.pokemonForm.type1Fight) || type.weak.includes(this.pokemonForm.type2Fight)) {
              if (!type.strong.includes(this.pokemonForm.type1Fight) && !type.strong.includes(this.pokemonForm.type2Fight)) {
                this.efficiency = 0.5;
              }
            }
            if (type.weak.includes(this.pokemonForm.type1Fight) && type.weak.includes(this.pokemonForm.type2Fight)) {
              this.efficiency = 0.25;
            }
            if (type.strong.includes(this.pokemonForm.type1Fight) || type.strong.includes(this.pokemonForm.type2Fight)) {
              if (!type.weak.includes(this.pokemonForm.type1Fight) && !type.weak.includes(this.pokemonForm.type2Fight)) {
                this.efficiency = 2;
              }
            }
            if (type.strong.includes(this.pokemonForm.type1Fight) && type.strong.includes(this.pokemonForm.type2Fight)) {
              this.efficiency = 4;
            }
          }
        }
        this.result = ((((2 * this.pokemonForm.level) / 5) + 2) * this.dbMove.power * (this.pokemonForm.atk / this.pokemonForm.def)) / 50 + 2
        var modifier = (this.critic ? 1.5 : 1) * this.getRand() * this.STAB * this.efficiency * this.burn
        this.result *= modifier
        this.result = Math.round(this.result)
        this.efficiency = 1;
      },
      error => {
        console.error(error);
      }
    );
  }

  ngOnInit(): void {
    this.pokemonForm.isValid = false;
    console.log("Auto-complete, metttere errori")
    if (localStorage.length == 0) {
      this.apiService.getMoves().subscribe(
        (data: apiMoves) => {
          data.results.forEach(element => {
            this.apiService.getUrl(element.url).subscribe(
              (data: apiMove) => {
                if (data.damage_class.name != "status" && data.power != null) {
                  if (data.id > 10000) {
                    localStorage.setItem(data.id.toString(), data.names[4].name)
                  } else {
                    localStorage.setItem(data.id.toString(), data.names[6].name)
                  }
                }
              }
            )
          })
        }
      )
    }
    if (this.myStorage == null) {
      this.myStorage = localStorage;
    }

    this.apiService.getTranslations().subscribe(
      (data: translations) => {
        this.trans = data
      }
    )
  }

  getMoveLocalized() {
    for (let i = 0; i < localStorage.length; i++) {
      let key = localStorage.key(i);
      let value = localStorage.getItem(key);
      if (this.pokemonForm.move.toLowerCase() == value.toLowerCase()) {
        return key
      }
    }
  }

  arrayStorage(n: number): number[] {
    return [...Array(n).keys()]
  }
}
