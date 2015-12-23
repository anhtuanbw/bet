'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, $modalInstance, toaster) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;

    // This function just add to test, will remove in next commit
    this.states = this.tournamentService.findAll()
      .then(response => {
        if (response.status === 200) {
          var tournaments = response.data;
          console.log(tournaments);
         return tournaments.map( function (repo) {
          repo.value = repo.name.toLowerCase();
          return repo;
        });
        } else {
        }
      });

    this.querySearch = this.querySearch;
    this.selectedItemChange = this.selectedItemChange;
    this.groupData = {};
    this.error = {};
  }

  create() {
    var self = this;
    self.popTitle = 'Title';

    self.pop = function(type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.groupService.create(self.groupData)
    .then(response => {
      if (response.status === 200) {
        self.closeGroup();
        self.pop('success', self.popTitle, 'Create success');
      } else {
        self.pop('warning', self.popTitle, response.data.message);
      }
    })
    .catch(response => {
      self.pop('error', self.popTitle, response.data.message);
    });
  }

  querySearch(query) {
    var results = query ? this.states.filter( this.createFilterFor(query) ) : this.states;
    return results;
  }

  selectedItemChange(item) {
    if(item) {
      this.groupData.moderator = item.id;
    }
  }

  getTournament() {
    this.tournamentService.findAll()
    .then(response => {
      if (response.status === 200) {
        var tournaments = response.data;
        console.log(tournaments);
       return tournaments.map( function (repo) {
        repo.value = repo.name.toLowerCase();
        return repo;
      });
      } else {
      }
    });
  }

  createFilterFor(query) {
    
    var lowercaseQuery = angular.lowercase(query);
    return function filterFn(state) {
      return (state.name.indexOf(lowercaseQuery) === 0);
    };
  }

  closeGroup() {
    this.modalInstance.dismiss();
  }

}