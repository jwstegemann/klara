'use strict';

/* App Module */

var requests = 0;
var loaderTimeout;

angular.module('klara', ['klaraFilters','klaraDirectives','http-auth-interceptor','$strap.directives']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/schueler', {templateUrl: 'partials/schuelerList.html', controller: SchuelerListCtrl}).
      when('/schueler/create', {templateUrl: 'partials/schuelerDetail.html', controller: SchuelerDetailCtrl}).
      when('/schueler/detail/:schuelerId', {templateUrl: 'partials/schuelerDetail.html', controller: SchuelerDetailCtrl}).
      otherwise({redirectTo: '/schueler'});
  }]).config(function ($httpProvider) {
      $httpProvider.responseInterceptors.push('msgHttpInterceptor');
      var spinnerFunction = function (data, headersGetter) {
          //$("#busy").show();

/*          $.blockUI({ css: { 
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        } }); 
*/
          if (requests++ == 0) {
            console.log("start " + requests);
            loaderTimeout = setTimeout("$('#busy').show()",300);
          }

          return data;
      };
      $httpProvider.defaults.transformRequest.push(spinnerFunction);
  }).run(['$rootScope', function($rootScope) {
  
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
  }]).factory('dictionary', function ($http, $q) {
    var dictionaryService = {};
    
    dictionaryService.cache = {};

    function mapDictionary(response) {
      var dict = {};
      angular.forEach(response, function(keyValue, index) {
        dict[keyValue.key] = keyValue.shortText;
      });
      return dict;      
    }

    dictionaryService.reload = function(name) {
      var deferred = $q.defer();
      dictionaryService.cache[name] = deferred.promise;

      $http.get("/dict/" + name).success(function(response) {
        deferred.resolve(mapDictionary(response));
      });
    }

    dictionaryService.get = function(name) {
      if (dictionaryService.cache[name] == undefined) {
        dictionaryService.reload(name);
      }
      return (dictionaryService.cache[name]);
    };

    return dictionaryService; 
  }).factory('message', function() {
    var messageService = {};
    
    messageService.notice = function(title, text) {
      $.pnotify({
        title: title,
        text: text,
        styling: 'bootstrap'
      });
    }

    messageService.info = function(title, text) {
      $.pnotify({
        title: title,
        text: text,
        type: 'info',
        styling: 'bootstrap'
      });
    }

    messageService.success = function(title, text) {
      $.pnotify({
        title: title,
        text: text,
        type: 'success',
        styling: 'bootstrap'
      });
    }

    messageService.error = function(error, status) {
      var details = "";
      if (angular.isDefined(error.details)) details = error.details;
      $.pnotify({
        title: error.text,
        text: details + " (" + status + ")",
        type: 'error',
        styling: 'bootstrap'
      });
      console.log("Fehler:" + angular.toJson(error, true));
    }

    messageService.fatal = function(text, status) {
      $.pnotify({
        title: "Technischer Fehler",
        text: text + " (" + status + ").<br>Bitte wenden Sie sich an Ihren Systemadministrator.",
        type: 'error',
        styling: 'bootstrap'
      });
      console.log("Technischer Fehler:" + text + " (" + status + ")");
    }

    return messageService; 
  }).factory('msgHttpInterceptor', function ($q, $window, message) {
    return function (promise) {
      return promise.then(function (response) {
        //$('#mydiv').hide();
        if (--requests <= 0) {
          requests = 0;
          console.log("stop success");
          clearTimeout(loaderTimeout);
          $("#busy").hide();
        }
        return response;
      }, function (response) {
        var contentType = response.headers("Content-Type");
        if (angular.isDefined(contentType) && (angular.lowercase(contentType).indexOf("application/json") != -1)) {
          if (angular.isDefined(response.data) && angular.isArray(response.data)) {
            angular.forEach(response.data, function(msg, index) {
              switch(msg.severity) {
                case "DEBUG": message.notice(msg.text, msg.details); break;
                case "INFO": message.notice(msg.text, msg.details); break;
                case "WARN": message.notice("Achtung:" + msg.text, msg.details); break;
                case "FATAL": message.error(msg, response.status); break;
                default: message.error(msg, response.status); break;
              }
              
            });
          }
        } else {
          message.fatal("in der Kommunikation mit dem Server: " + response.data, response.status);
        }
       
        //$('#mydiv').hide();
        if (--requests <= 0) {
          requests = 0;
          console.log("stop error");
          clearTimeout(loaderTimeout);
          $("#busy").hide();
        }
        return $q.reject(response);
      });
    };
  });;
