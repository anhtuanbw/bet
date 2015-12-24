'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, $mdDialog) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.mdDialog = $mdDialog;

    this.moderators = this.getModerators();
    this.getTournaments();
    this.selectedItem = null;
    this.searchText = null;
    this.querySearch = this.querySearch;
    this.groupData = {};
    this.error = {};
  }

  getTournaments() {
    var self = this;
    return this.tournamentService.findAll()
      .then(response => {
        if (response.status === 200) {
          self.groupData.tournament = response.data
        }
      });
  }

  getModerators() {
   return this.tournamentService.findAll()
      .then(response => {
        if (response.status === 200) {
          var tournaments = response.data;
          return tournaments.map( function (repo) {
            return repo;
          });
        }
      });
  }

  querySearch(query) {
    var self = this;
    return query ? self.moderators.filter(self.createFilterFor(query) ) : self.moderators;
  }

  createFilterFor(query) {
    var lowercaseQuery = angular.lowercase(query);
    return function filterFn(state) {
      return (state.value.indexOf(lowercaseQuery) === 0);
    };
  }

  save($event) {
    // Create group
  }

  cancel($event) {
    this.mdDialog.cancel();
  }

}