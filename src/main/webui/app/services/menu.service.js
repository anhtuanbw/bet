'use strict';

export default class MenuService {
  /* @ngInject */
  constructor($location, TournamentService) {
    this.location = $location;
    this.tournamentService = TournamentService;
    this.openedSection = null;
    this.sections = null;
    this.getAllTournament();
  }

  getAllTournament() {
    this.tournamentService.findByRole()
      .then(response => {
        this.sections = response.data;
      })
      .catch(error => {
        if (error.status === 401) {
          this.location.path('/unauthorized');
        }
      });
  }

  toggleSelectSection(section) {
    this.openedSection = (this.openedSection === section ? null : section);
  }

  isSectionSelected(section) {
    return this.openedSection === section;
  }

  // selectPage(section, page) {
  //   page && page.url && this.location.path(page.url);
  //   this.currentSection = section;
  //   this.currentPage = page;
  // }

}