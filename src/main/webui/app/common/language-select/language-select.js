'use strict';

class LanguageSelectController {
  /* @ngInject */
  constructor($translate, $rootScope, $cookies) {
    this.$translate = $translate;
    this.rootScope = $rootScope;
    this.cookies = $cookies;
    this.initLanguage =  this.cookies.get('NG_TRANSLATE_LANG_KEY') ? this.cookies.get('NG_TRANSLATE_LANG_KEY') : 'en_US';
    this.systemLanguage = this.$translate.use() || this.initLanguage;
    this.changeLanguage();
  }
  
  changeLanguage() {
    this.$translate.use(this.systemLanguage);
    this.cookies.put('NG_TRANSLATE_LANG_KEY', this.systemLanguage);
    this.rootScope.$broadcast('changeLang', this.systemLanguage);
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
