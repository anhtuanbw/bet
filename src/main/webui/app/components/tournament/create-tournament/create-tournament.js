'use strict';

export default class CreateTournamentController {
  constructor(TournamentService, toaster) {
    this.tournamentService = TournamentService;
    this.competitors = [];
    this.name = '';
    this.errorMessage = {};
    this.isActive = false;
    this.toaster = toaster;
  }
  getNumOfCompetitors() {
    return this.competitors.length;
  }
  
  getData() {
    var data = {};
    data.name = this.name;
    data.competitors = [];
    data.isActive = this.isActive;
    for( var i in this.competitors) {
      data.competitors.push(this.competitors[i].text);
    }
    return data;
  }
  
  createTournament() {
    var tournamentInfo = this.getData();
    this.tournamentService.createTournament(tournamentInfo)
    .then(() => {
      this.toaster.pop('success', null, "app/components/tournament/create-tournament/success.html", null, 'template');
      this.competitors = [];
      this.name = '';
      this.errorMessage = {};
    })
    .catch(error => {
       if (error.status === 400) {
        this.errorMessage = error.data.fieldErrors;
      }
    });
  }
}