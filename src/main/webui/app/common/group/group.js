'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, AccountService, $mdDialog, toaster) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.accountService = AccountService;
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
    this.tournamentService.getAll()
    .then(response => {
      if (response.status === 200) {
        self.groupData.tournaments = response.data;
      }
    });
  }

  getModerators() {
   return this.accountService.users()
   .then(response => {
    if (response.status === 200) {
      var users = response.data;
      return users.map( function (repo) {
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
      moderator: this.selectedItem.id ? this.selectedItem.id : null
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