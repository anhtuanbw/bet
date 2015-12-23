'use strict';

export default class GroupController {
  /* @ngInject */
  constructor(GroupService, TournamentService, $modalInstance, toaster) {
    var self = this;
    this.groupService = GroupService;
    this.tournamentService = TournamentService;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;

    this.states = this.tournamentService.findAll()
    .then(response => {
      if (response.status === 200) {
        var tournaments = response.data;
        console.log(tournaments);
       return tournaments.map( function (repo) {
        console.log("--map--");
        repo.value = repo.name.toLowerCase();
        return repo;
      });
      } else {
      }
    })
    .catch(response => {
    });
//this.states = [{ name : 'euro', id : 1},{ name : 'world cup' , id : 2}];
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
    console.log(typeof this.states);
    var results = query ? this.states.filter( this.createFilterFor(query) ) : this.states, deferred;
    return results;
  }

  selectedItemChange(item) {
    if(item) {
      this.groupData.moderator = item.id;
    }
  }

//   getTournament() {
// //return  [{ name : 'euro', id : 1},{ name : 'world cup' , id : 2}];
//     this.tournamentService.findAll()
//     .then(response => {
//       if (response.status === 200) {
//         var tournaments = response.data;
//         console.log(tournaments);
//        return tournaments.map( function (repo) {
//         console.log("--map--");
//         repo.value = repo.name.toLowerCase();
//         return repo;
//       });
//       } else {
//       }
//     })
//     .catch(response => {
//     });
//   }

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