'use strict';

/* Directives */


angular.module('klaraDirectives', []).directive('onKeyUp', function() {
    return function(scope, elm, attrs) {
        //Evaluate the variable that was passed
        //In this case we're just passing a variable that points
        //to a function we'll call each keyup
        var keyupFn = scope.$eval(attrs.onKeyUp);
        elm.bind('keyup', function(evt) {
            //$apply makes sure that angular knows 
            //we're changing something
            scope.$apply(function() {
                keyupFn.call(scope, evt.which);
            });
        });
    };
}).directive('onEnter', function() {
    return function(scope, elm, attrs) {
        function applyKeyup() {
          scope.$apply(attrs.onEnter);
        };           
        
        var allowedKeys = scope.$eval(attrs.keys);
        elm.bind('keyup', function(evt) {
            //if no key restriction specified, always fire
            if (evt.which == 13) {
                applyKeyup();
            } 
        });
    };
}).directive('entityTable', function() {
    return {
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: { 
            createLink: '@create',
            deleteFunction: '&delete',
            reloadFunction: '&reload',
            entities: '=entities',
            columns: '=columns',
            editLinkPrefix: '@editPrefix'
        },
        controller: function($scope, $element, $attrs, $filter) {
            $scope.selectAllValue = false;
            $scope.sortReverse = false;
//            $scope.sortPredicate = "name"; //TODO: init-Wert
            $scope.colspan = $scope.columns.length + 1;

            $scope.selectAll = function() {
                angular.forEach($scope.entities, function(item, index) {
                    item.selected = $scope.selectAllValue;
                });
            } 

            $scope.sort = function(attribute) {
                if (attribute == $scope.sortPredicate) {
                  $scope.sortReverse = !$scope.sortReverse;
                }
                else {
                  $scope.sortPredicate = attribute;
                  $scope.sortReverse = false;
                }
            }

            $scope.selectedItems = function() {
              var visibleItems = $filter('filter')($scope.entities, $scope.filterValue);
              return $filter('filter')(visibleItems, {selected: true});
            }

            $scope.deleteSelected = function() {
              angular.forEach($scope.selectedItems(), function(item, index) {
                $scope.deleteFunction({id: item._id});
              });
              $scope.reloadFunction();
            }            
        },
        templateUrl: "templates/entityTable.html",
        compile: function($element, $attr, $linker) {
        },
        link: function($scope, $element, $attrs) {
/*
             * handle resizable columns
             */
            var pressed = false;
            var start = undefined;
            var startX, startWidth;

            // prevent sorting when resizing cols
            $element.find("thead tr th .sizehandler").click(function(e) {
              e.stopImmediatePropagation()
            });
            
            $element.find("thead tr th .sizehandler").mousedown(function(e) {
                start = $(this).parent();
                pressed = true;
                startX = e.pageX;
                startWidth = start.width();

                e.preventDefault();

                $(start.parent().mousemove(function(e) {
                  if(pressed) {
                      $(start).width(startWidth+(e.pageX-startX));
                      e.preventDefault();
                  }
                }));
            });

            $(document).mouseup(function(e) {
                e.preventDefault();

                if(pressed) {
                    pressed = false;
                    start.parent().off('mousemove');
                }
            });            
        

        }
    }
})

/*.directive('selectAll', function() {
    return {
        restrict: 'A',
        replace: false,
        transclude: true,
        scope: { model: '=model'},
        controller: function($scope, $element, $attrs) {
            $scope.selectValue = false;

            $scope.selectAll = function() {
                angular.forEach($scope.model, function(item, index) {
                    item.selected = $scope.selectValue;
                });
            }
        },
        template: '<input type="checkbox" id="select_all" ng-model="selectValue" ng-change="selectAll()" />',
//        compile: function(element, attr, linker) {
//        },
        link: function($scope, $element, $attrs) {


        }
    }
}).directive('resizableColumns', function() {
    return {
        restrict: 'A',
        replace: false,
        transclude: false,
        compile: function($element, $attr, $linker) {
            var pressed = false;
            var start = undefined;
            var startX, startWidth;

            // prevent sorting when resizing cols
            $element.find("thead tr th .sizehandler").click(function(e) {
              e.stopImmediatePropagation()
            });
            
            $element.find("thead tr th .sizehandler").mousedown(function(e) {
                start = $(this).parent();
                pressed = true;
                startX = e.pageX;
                startWidth = start.width();

                e.preventDefault();

                $(start.parent().mousemove(function(e) {
                  if(pressed) {
                      $(start).width(startWidth+(e.pageX-startX));
                      e.preventDefault();
                  }
                }));
            });

            $(document).mouseup(function(e) {
                e.preventDefault();

                if(pressed) {
                    pressed = false;
                    start.parent().off('mousemove');
                }
            });            
        }
    }
})
*/



