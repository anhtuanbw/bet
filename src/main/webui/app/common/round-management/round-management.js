'use strict';

export default class roundManController {
  /* @ngInject */
  constructor($scope, RoundService) {
    this.scope = $scope;
    this.RoundService = RoundService;
    this.tourCompetitor = [];
    this.tourID = 0;
    this.roundCompetitor = [];
    this.roundSave = {};
  }


  addCompetitor(round) {
    var checkExist = round.competitorList.indexOf(round.competitorSelected);
    var compType = typeof round.competitorSelected;
    round.roundError = '';
    if ( checkExist === -1 && compType !== 'undefined' && round.competitorSelected !== '') {
        round.competitorList.push(round.competitorSelected);
        this.pushCompetitorIdToList(this.tourCompetitor, this.roundCompetitor, round.competitorSelected);
        var ind = round.competitorInComboBox.indexOf(round.competitorSelected);
        round.competitorInComboBox.splice(ind, 1);
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

  selectTour(round){
    //Load depend Competitor follow selected tour
    //remove old Competitor list, comboBox and round name
    round.competitorList = [];
    round.competitorInComboBox = [];
    round.name = '';
    round.roundError = '';
    round.CompetitorError = '';
    round.success = '';
    //find tournament ID
    this.RoundService.getAllTournament()
    .then(response => {
      var ind;
      for (var i = 0; i < response.data.length; i++) {
        if (response.data[i].name === round.tour){
          ind = i;
         this.tourID = response.data[i].id;
        }
      }
      //add competitors to comboBox
      this.RoundService.getAllCompetitor(this.tourID)
      .then(response => {
        this.tourCompetitor = response.data;
        for (var j = 0; j < response.data.length; j++) {
          round.competitorInComboBox.push(response.data[j].name);
        }
      });
    });
  }

  loadTour(round){
    this.RoundService.getAllTournament()
    .then(response => {
      //success
      var i;
      for (i = 0; i < response.data.length; i++) {
          round.tourlist.push(response.data[i].name);
      }
    });
  }

  saveData(roundData){
    this.roundSave = {
      'name': roundData.name,
      'tournamentId': this.tourID,
      'competitorIds': this.roundCompetitor
    };
    roundData.CompetitorError = '';
    roundData.roundError = '';
    this.RoundService.create(this.roundSave)
    .then(response => {
      roundData.success = 'Saved Successfully !!!';
      //remove all old data
      roundData.competitorList = [];
      roundData.competitorInComboBox = [];
      roundData.tour = [];
      roundData.name = '';
    }, function (response) {
      roundData.roundError = response.data.fieldErrors.name;
      roundData.CompetitorError = response.data.fieldErrors.competitorId;
    });
  }
}

export default class Round {
  constructor () {
    return {
      controller: roundManController,
      controllerAs: 'round',
      templateUrl: 'app/common/round-management/round-management.html'
    };
  }
}
