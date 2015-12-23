'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, $modalInstance, toaster) {
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.states = this.getTournament();
    this.querySearch = this.querySearch;
    this.selectedItemChange = this.selectedItemChange;
    this.groupData = {};
    this.error = {};
  }

  create() {
    var self = this;
    self.popTitle = 'Title';

    // Show alert message
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
    var results = query ? this.states.filter( this.createFilterFor(query) ) : this.states, deferred;
    return results;
  }

  selectedItemChange(item) {
    if(item) {
      this.groupData.moderator = item.value;
    }
  }

  getTournament() {
    this.tournamentService.findAll()
    .then(response => {
      if (response.status === 200) {
        var tournaments = response.data;

        var a = tournaments.map(function(tournament) {
          return {
            value: tournament.id,
            display: tournament.name
          }
        });

        console.log(a);
        return a;
      } else {
      }
    })
    .catch(response => {
    });
    // return allStates.split(/, +/g).map( function (state) {
    //   return {
    //     value: state.toLowerCase(),
    //     display: state
    //   };
    // });
  }

  createFilterFor(query) {
    var lowercaseQuery = angular.lowercase(query);
    return function filterFn(state) {
      return (state.value.indexOf(lowercaseQuery) === 0);
    };
  }

  closeGroup() {
    this.modalInstance.dismiss();
  }

}