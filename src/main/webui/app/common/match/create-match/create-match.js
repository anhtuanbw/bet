'use strict';

export default class CreateMatchController {
  /* @ngInject */
  constructor(MatchService, CacheService, $location, $modalInstance, toaster, editId, $rootScope) {
    this.rootScope = $rootScope;
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.data = {};
    this.dataRounds = [];
    this.dataCompetitors = [];
    this.tournamentId = editId;
    this.getRounds();
  }

  createMatch() {
    var self = this;
    this.popTitle = 'Create new match';
    var successMessage = 'Create match successfully!';
    // Show alert message
    this.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
    var time = this.data.time;
    this.data.time = this.formatTime(this.data.time);
    this.matchService.createMatch(this.data)
      .then(() => {

        // Success
        this.closeModal();
        this.pop('success', this.popTitle, successMessage);
        this.data = {};
        successMessage = '';
        this.rootScope.$broadcast('selectTournament');
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
}
