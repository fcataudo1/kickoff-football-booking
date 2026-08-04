import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldListComponent } from './field-list';


describe('FieldListComponent', () => {

  let component: FieldListComponent;
  let fixture: ComponentFixture<FieldListComponent>;


  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        FieldListComponent
      ]

    }).compileComponents();


    fixture = TestBed.createComponent(FieldListComponent);

    component = fixture.componentInstance;

    await fixture.whenStable();

  });



  it('should create', () => {

    expect(component).toBeTruthy();

  });

});
