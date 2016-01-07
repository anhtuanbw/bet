'use strict';

export default class roundManController {
  /* @ngInject */
  constructor($scope, RoundService, toaster, tourID, selectedRound, $modalInstance) {
    this.scope = $scope;
    this.RoundService = RoundService;
    this.tourCompetitor = [];
    this.roundCompetitor = [];
    this.roundSave = {};
    this.toaster = toaster;
    this.modalInstance = $modalInstance;
    this.tourID = tourID;
    this.selectedRound = selectedRound;
    this.data = {};
    this.loadTour();
    this.addCompetitorToComboBox();
    this.oldCompetitor = [];
    this.roundID = 0;
  }

  loadTour(){
    this.RoundService.getAllTournament()
    .then(response => {
      for (var i = 0; i < response.data.length; i++) {
        if (response.data[i].id === this.tourID) {
          this.data.tournamentName = response.data[i].name;
        }
      }
    });
    this.data.hide = this.selectedRound.hide;
    this.RoundService.getRoundInTournament(this.tourID)
    .then(response => {
      for (var i = 0; i < response.data.length; i++) {
        if (response.data[i].id === this.selectedRound.roundId) {
          this.oldCompetitor = response.data[i].competitors;
          this.loadOldCompetitorToList(this.oldCompetitor);
          this.data.name = response.data[i].name;
          this.roundID = response.data[i].id;
        }
      }
    });
  }

  addCompetitorToComboBox(){
    this.data.competitorInComboBox = [];
    this.RoundService.getAllCompetitor(this.tourID)
    .then(response => {
      this.tourCompetitor = response.data;
      for (var j = 0; j < response.data.length; j++) {
        this.data.competitorInComboBox.push(response.data[j].name);
      }
    });
  }

  loadOldCompetitorToList(oldCompetitor){
    for (var i = 0; i < oldCompetitor.length; i++) {
      this.data.competitorOldList.push(oldCompetitor[i].name);
    };
  }

  addCompetitor(round) {
    var checkExist = round.competitorList.indexOf(round.competitorSelected);
    var compType = typeof round.competitorSelected;
    round.roundError = '';
    if ( checkExist === -1 && compType !== 'undefined' && round.competitorSelected !== '') {
        round.competitorList.push(round.competitorSelected);
        this.pushCompetitorIdToList(this.tourCompetitor, this.roundCompetitor, round.competitorSelected);
        round.competitorSelected = '';
        round.CompetitorError = '';
    } else if (typeof round.competitorSelected === 'undefined' || round.competitorSelected === '') {
        round.CompetitorError = 'Please select Competitor !!!';
    } else {
        round.CompetitorError = 'Competitor already exist !!!';
    }
  }

  pushCompetitorIdToList(competitorInTour, competitorInRound, competitorName ){
    for (var k = 0; k < competitorInTour.length; k++) {
      if (competitorInTour[k].name == competitorName) {
        competitorInRound.push(competitorInTour[k].id);
      }
    }
  }

  remove(round,ind){
    round.competitorList.splice(ind, 1);
    this.roundCompetitor.splice(ind, 1);
  }

  saveData(roundData){
    var popTitle = 'Round Management';
    this.roundSave = {
      'name': roundData.name,
      'tournamentId': this.tourID,
      'competitorIds': this.roundCompetitor
    };
    roundData.CompetitorError = '';
    roundData.roundError = '';
    this.RoundService.create(this.roundSave)
    .then(() => {
      this.toaster.pop('success', popTitle, 'Create Round Successfully !!!');
      //remove all old data
      roundData.competitorList = [];
      roundData.competitorInComboBox = [];
      roundData.tour = [];
      roundData.name = '';
      this.modalInstance.dismiss();
    }, function (response) {
      roundData.roundError = response.data.fieldErrors.name;
      roundData.CompetitorError = response.data.fieldErrors.competitorId;
    });
  }

  updateData(){
    var popTitle = 'Update Round';
    var dataUpdate = {
        'roundId': this.roundID,
        'competitorIds': this.roundCompetitor
    };
    this.RoundService.update(dataUpdate)
    .then(() => {
      //success
      this.toaster.pop('success', popTitle, 'Update Successfully !!!');
      this.modalInstance.dismiss();
    }, function(response){
      this.toaster.pop('error', popTitle, response.data.fieldErrors);
    });
  }

  cancel(){
    this.modalInstance.dismiss();
  }

}
