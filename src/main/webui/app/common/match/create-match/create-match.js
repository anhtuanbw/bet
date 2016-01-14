'use strict';

export default class CreateMatchController {
  /* @ngInject */
  constructor(MatchService, CacheService, RoundService, $location, $modalInstance, toaster, editId, $rootScope, $modal) {
    this.rootScope = $rootScope;
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.RoundService = RoundService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.modal = $modal;
    this.toaster = toaster;
    this.data = {};
    this.dataRounds = [];
    this.dataCompetitors = [];
    this.tournamentId = editId;
    this.checkRoundNull = false;
    this.showModal = false;
    this.getCompetitorInTournament();
  }

  createMatch() {
    var self = this;
    self.popTitle = 'Create new match';
    
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    var time;
    if (!this.data.time) {
      this.data.time = '';
    }
    if (this.data.time !== '') {
      time = this.data.time;
      this.data.time = this.formatTime(this.data.time);
    }
    this.matchService.createMatch(this.data)
      .then(() => {

        // Success
        this.closeModal();
        this.toaster.pop('success', null, 'app/common/match/create-match/success.html', null, 'template');
        this.data = {};
        this.rootScope.$broadcast('newMatch');
      })
      .catch(response => {
        self.data.time = time;
        // return error
        if (response.data.message) {
          self.pop('error', self.popTitle, response.data.message);
        }
        self.errorMessage = response.data.fieldErrors;
      });
  }

  getRounds() {
    this.matchService.getRounds(this.tournamentId)
      .then(response => {
        
        // Success
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataRounds.push(response.data[i]);
        }
      });
  }

  getCompetitors() {
    this.dataCompetitors = [];
    this.matchService.getCompetitors(this.data.round)
      .then(response => {

        // Success
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataCompetitors.push(response.data[i]);
        }
      });
  }

  getCompetitorsInTournament() {
    this.RoundService.getAllCompetitor(this.tournamentId)
      .then(response => {
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataCompetitors.push(response.data[i]);
        }
      });
  }

  getCompetitorInTournament() {
    this.matchService.checkRound(this.tournamentId)
      .then(response => {
        this.checkRoundNull = response.data;
        if (this.checkRoundNull) {
          this.getRounds();

        } else {
          this.showModal = true;
          this.getCompetitorsInTournament();
        }
      });
  }

  changeTime(time) {
    return (time.length === 2 ? time : '0' + time[0]);
  }

  formatTime(time) {
    var timeFormat = new Date(time);
    var year = timeFormat.getFullYear(),
      month = (timeFormat.getMonth() + 1).toString(),
      date = timeFormat.getDate().toString(),
      hour = timeFormat.getHours().toString(),
      minute = timeFormat.getMinutes().toString();

    return year + '-' + this.changeTime(month) + '-' + this.changeTime(date) + 'T' +
      this.changeTime(hour) + ':' + this.changeTime(minute);
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }

  closeRoundEmptyModal() {
    this.showModal = false;
  }

}
