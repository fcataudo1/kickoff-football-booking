import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FootballFieldService } from '../../services/football-field.service';
import { FootballField } from '../../models/football-field';


@Component({
  selector: 'app-field-list',
  standalone: true,
  imports: [],
  templateUrl: './field-list.html',
  styleUrl: './field-list.css'
})
export class FieldListComponent implements OnInit {


  @Output()
  selectField = new EventEmitter<number>();


  fields: FootballField[] = [];


  constructor(
    private footballFieldService: FootballFieldService
  ){}


  reserve(id:number){

    this.selectField.emit(id);

  }



  ngOnInit(){

    this.footballFieldService.getAll()
    .subscribe({

      next:(data)=>{

        this.fields=data;

      },

      error:(err)=>{

        console.error(err);

      }

    });

  }

}