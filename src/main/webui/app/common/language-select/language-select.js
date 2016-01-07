'use strict';

class LanguageSelectController {
  /* @ngInject */
  constructor($translate, CacheService, $rootScope, $cookies) {
    this.$translate = $translate;
    this.cookies = $cookies;
    this.systemLanguage = this.$translate.use() || this.cookies.get('NG_TRANSLATE_LANG_KEY');
    this.cacheService = CacheService;
    this.rootScope = $rootScope;
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
