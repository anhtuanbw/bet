'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, $mdDialog, toaster) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.mdDialog = $mdDialog;
    this.toaster = toaster;

    this.moderators = this.getModerators();
    this.getTournaments();
    this.selectedItem = null;
    this.searchText = null;
    this.querySearch = this.querySearch;
    this.groupData = {};
    this.error = {};
  }

  pop(type, title, content) {
      this.toaster.pop(type, title, content);
  };

  getTournaments() {
    var self = this;
    this.tournamentService.findAll()
      .then(response => {
        if (response.status === 200) {
          self.groupData.tournaments = response.data;
        }
      });
  }

  getModerators() {
   return this.tournamentService.getAll()
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
    return query ? self.moderators.filter(this.createFilterFor(query) ) : self.moderators;
  }

  createFilterFor(query) {
    var lowercaseQuery = angular.lowercase(query);
    return function filterFn(state) {
      return (state.value.indexOf(lowercaseQuery) === 0);
    };
  }

  save($event) {
    var self = this;
    // Create group
    var data = {
      name: this.groupData.name,
      tournamentId: this.groupData.tournament,
      moderator: this.selectedItem.id
    };

    this.groupService.create(data)
      .then(response => {
        if (response.status === 200) {

        }
      })
      .catch(response => {
          self.error = response.data.fieldErrors;
          console.log(self.error);
          self.pop('error', '', response.data.fieldErrors);
      });
  }

  cancel($event) {
    this.mdDialog.cancel();
  }

}