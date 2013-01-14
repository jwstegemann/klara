'use strict';

/* App Module */

angular.module('klara', ['klaraFilters','http-auth-interceptor']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/schueler', {templateUrl: 'partials/schuelerList.html', controller: SchuelerListCtrl}).
      when('/schuelerDetail/:schuelerId', {templateUrl: 'partials/schuelerDetail.html', controller: SchuelerDetailCtrl}).
      otherwise({redirectTo: '/schueler'});
  }]).run(['$rootScope', function($rootScope) {
  		alert('Bin da!');
  
       var login = $('#login-holder');

       login.hide();

       $rootScope.$on('event:auth-loginRequired', function() {
          login.slideDown('slow', function() {
          });
        });
        $rootScope.$on('event:auth-loginConfirmed', function() {
          login.slideUp();
        });
  }]);
