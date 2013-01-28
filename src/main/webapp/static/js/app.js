'use strict';

/* App Module */

angular.module('klara', ['klaraFilters','klaraDirectives','http-auth-interceptor']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/schueler', {templateUrl: 'partials/schuelerList.html', controller: SchuelerListCtrl}).
      when('/schuelerDetail/:schuelerId', {templateUrl: 'partials/schuelerDetail.html', controller: SchuelerDetailCtrl}).
      otherwise({redirectTo: '/schueler'});
  }]).run(['$rootScope', function($rootScope) {
  
       var login = $('#login-holder');

       login.modal({
          keyboard: false,
          backdrop: "static",
          show: false
       });

       login.on('shown', function () {
          $("#loginUsername").focus();
       })

       // make loginButton submit the form
       $('#loginButton').click(function(e) {
         e.preventDefault();
         $("#loginForm").submit();
       });

       $rootScope.$on('event:auth-loginRequired', function() {
          login.modal('show');
        });
        $rootScope.$on('event:auth-loginConfirmed', function() {
          login.modal('hide');
        });
  }]);
