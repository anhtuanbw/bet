'use strict';

export default function () {
  return {
    require: '?ngModel',
    restrict: 'A',
    link: function (scope, element, attrs, ngModel) {

      if (!ngModel) return; // do nothing if no ng-model

      ngModel.$render = function () {
        element.find('input').val(ngModel.$viewValue || '');
      };

      element.datetimepicker({
        format: 'YYYY-MM-DD HH:mm'
      });

      element.on('dp.change', function () {
        scope.$apply(read);
      });

      function read() {
        var value = element.find('input').val();
        ngModel.$setViewValue(value);
      }
      
      read();
    }
  };
}
