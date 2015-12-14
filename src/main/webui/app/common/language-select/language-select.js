'use strict';

class LanguageSelectController {
  /* @ngInject */
  constructor($translate) {
    this.$translate = $translate;
    this.systemLanguage = this.$translate.use() || 'en_US';
  }
  
  changeLanguage() {
    this.$translate.use(this.systemLanguage);
  }
}

export default class LanguageSelect {
  constructor() {
    return {
      replace: true,
      scope: true,
      bindToController: {
        languages: '=languageSelect'
      },
      controller: LanguageSelectController,
      controllerAs: 'languageSelect',
      templateUrl: 'app/common/language-select/language-select.html'
    };
  }
}
