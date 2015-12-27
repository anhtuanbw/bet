/* global $ */
'use strict';

export default function () {
  return {
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      $(function () {
      element.datetimepicker({
        format: 'YYYY-MM-DD hh:mm A'
        });
      });
    }
  }
}