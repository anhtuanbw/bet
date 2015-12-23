/* global $ */
'use strict';

export default function () {
  return {
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      $(function () {
      element.datetimepicker({
        format: 'MM-DD-YYYY hh:mm A'
        });
      });
    }
  }
}