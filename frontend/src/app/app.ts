import { Component } from '@angular/core';


import { NavbarComponent } from './components/navbar/navbar';
import { HeroComponent } from './components/hero/hero';
import { BookingComponent } from './components/booking/booking';
import { ServicesComponent } from './components/services/services';
import { VideoSectionComponent } from './components/video-section/video-section';
import { WhyKickoffComponent } from './components/why-kickoff/why-kickoff';
import { StatsComponent } from './components/stats/stats';
import { ContactComponent } from './components/contact/contact';
import { FooterComponent } from './components/footer/footer';



@Component({

  selector:'app-root',

  standalone:true,

  imports:[

    NavbarComponent,

    HeroComponent,

    BookingComponent,

    ServicesComponent,

    VideoSectionComponent,

    WhyKickoffComponent,

    StatsComponent,

    ContactComponent,

    FooterComponent

  ],

  templateUrl:'./app.html',

  styleUrl:'./app.css'

})


export class App {


}