'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, UserService, $mdDialog, toaster) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.userService = UserService;
    this.mdDialog = $mdDialog;
    this.toaster = toaster;

    this.moderators = this.getModerators();
    this.getTournaments();
    this.selectedItem = null;
    this.querySearch = this.querySearch;
    this.groupData = {};
    this.error = {};
  }

  getTournaments() {
    var self = this;
    this.tournamentService.getAll()
    .then(response => {
      if (response.status === 200) {
        self.groupData.tournaments = response.data;
      }
    });
  }

  getModerators(query) {
   return this.userService.users(query)
   .then(response => {
    if (response.status === 200) {
      var users = response.data;
      return users.map(function(user) {
        return user;
      });
    }
  });
 }

 querySearch(query) {
  return this.getModerators(query);
}

save() {
  // Create group
  var self = this;
  var data = {
    name: this.groupData.name,
    tournamentId: this.groupData.tournament,
    moderator: this.selectedItem ? this.selectedItem.id : null
  };

  this.groupService.create(data)
  .then(response => {
    if (response.status === 200) {
      this.toaster.pop('success', 'Create group', 'app/common/group/create-success.html', null, 'template');
      self.cancel();
    }
  })
  .catch(response => {
    self.error = response.data.fieldErrors;
  });
}

cancel() {
  this.mdDialog.cancel();
}

}